package analyzer.visitors;

import analyzer.ast.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;



public class PrintMachineCodeVisitor implements ParserVisitor {

    private PrintWriter m_writer = null;

    private int NODE = 0; // current node

    private ArrayList<Vector<String>> REGISTERS; // registers of all registers. Number of the register is his index

    private ArrayList<Vector<String>> IN; // ArrayList des variables vives IN à chaque noeud
    private ArrayList<Vector<String>> OUT; // ArrayList des variables vives OUT à chaque noeud

    private HashMap<String,String> OP; // HashMap des Opérations

    private ArrayList<String> USE_QUEUE; // potentiellement, stocker l'ordre d'utilisation des Registres (pour savoir lequel est le plus vieux)
    private ArrayList<String> MODIFIED; // potentiellement, garder une liste des variables modifiées (assigned) pour savoir s'il faut faire un ST

    public PrintMachineCodeVisitor(PrintWriter writer) {
        m_writer = writer;

        IN  = new ArrayList<Vector<String>>();
        OUT = new ArrayList<Vector<String>>();

        OP = new HashMap<>();
        OP.put("+", "ADD");
        OP.put("-", "MIN");
        OP.put("*", "MUL");
        OP.put("/", "DIV");

        // POTENTIELLEMENT utiliser ça... Pas obligatoire
        USE_QUEUE = new ArrayList<>();
        MODIFIED  = new ArrayList<>();
    }

    @Override
    public Object visit(SimpleNode node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTProgram node, Object data) {
        // Visiter les enfants
        node.childrenAccept(this, null);

        // TODO: vider REGISTERS (et faire les ST en conséquence)
        for(String variable :this.MODIFIED)
            if(this.OUT.get(this.NODE-1).contains(variable))
            this.printStore(variable);
        m_writer.close();
        return null;
    }


    @Override
    public Object visit(ASTNumberRegister node, Object data) {
        node.childrenAccept(this, null);

        // Création de REGISTERS : ArrayList de Vectors pour pouvoir stocker les alias
        REGISTERS = new ArrayList<Vector<String>>();
        for(int i=0; i <  ((ASTIntValue) node.jjtGetChild(0)).getValue(); i++) {
            REGISTERS.add(new Vector<String>());
        }
        return null;
    }


    @Override
    public Object visit(ASTLive node, Object data) {
        node.childrenAccept(this, null);
        NODE = 0; // reset du Numéro courant de NODE

        return null;
    }

    @Override
    public Object visit(ASTLiveNode node, Object data) {
        // Sauvegarde des variables vives lives dans IN et OUT
        int nbr_node = NODE++;
        Vector<String> live_in = ((ASTInNode) node.jjtGetChild(0)).getLive();
        IN.add(nbr_node, live_in);

        Vector<String> live_out = ((ASTOutNode) node.jjtGetChild(1)).getLive();
        OUT.add(nbr_node, live_out);

        return null;
    }

    @Override
    public Object visit(ASTInNode node, Object data) {
        node.childrenAccept(this, null);
        return null;
    }

    @Override
    public Object visit(ASTOutNode node, Object data) {
        node.childrenAccept(this, null);
        return null;
    }

    @Override
    public Object visit(ASTBlock node, Object data) {
        node.childrenAccept(this, null);
        return null;
    }

    @Override
    public Object visit(ASTStmt node, Object data) {
        node.childrenAccept(this, null);
        NODE++; // TODO: vous pouvez mettre ce numéro avant le Accept pour avoir les numéros de NODE commençant à 1.
        return null;
    }

    @Override
    public Object visit(ASTAssignStmt node, Object data) {
        // On ne visite pas les enfants puisque l'on va manuellement chercher leurs valeurs
        // On n'a rien a transférer aux enfants
        String assigned = (String) node.jjtGetChild(0).jjtAccept(this, null);
        String left = (String) node.jjtGetChild(1).jjtAccept(this, null);
        String right = (String) node.jjtGetChild(2).jjtAccept(this, null);

        String source1 = this.getReg(right, this.NODE,null );
        String source2 = this.getReg(left, this.NODE,null );

        String destination = this.getReg(assigned, this.NODE,null );

        this.printOp(node.getOp(),destination,source2,source1);
        // TODO: Chaque variable a son emplacement en mémoire, mais si elle est déjà dans un registre, ne la rechargez pas!
        // TODO: Si une variable n'est pas vive, ne l'enregistrez pas en mémoire.
        // TODO: Si vos registres sont pleins, déterminez quelle variable vous allez retirer et si vous devez la sauvegarder
        // TODO: Écrivez la traduction en code machine, une instruction intermédiaire peut générer plus qu'une instruction machine
        // TODO: Ici on aura toujours une Opération entre deux éléments (variables ou/et valeurs) stocké dans une variable.
        //       Utilisé la Map OP pour récupérer le nom de l'opération (ex: OP.get("/") => DIV)
        return null;
    }

    @Override
    public Object visit(ASTAssignUnaryStmt node, Object data) {
        // On ne visite pas les enfants puisque l'on va manuellement chercher leurs valeurs
        // On n'a rien a transférer aux enfants
        String assigned = (String) node.jjtGetChild(0).jjtAccept(this, null);
        String left = (String) node.jjtGetChild(1).jjtAccept(this, null);

        // TODO: même chose que ASTAssignStmt mais on aura toujours une expression 
        // du type "MIN #0, R*". Veuillez gérer ce cas aussi.
        return null;
    }

    @Override
    public Object visit(ASTAssignDirectStmt node, Object data) {
        // On ne visite pas les enfants puisque l'on va manuellement chercher leurs valeurs
        // On n'a rien a transférer aux enfants
        String assigned = (String) node.jjtGetChild(0).jjtAccept(this, null);
        String left = (String) node.jjtGetChild(1).jjtAccept(this, null);

        String source = this.getReg(left, this.NODE,null );
        int index = this.indexForRegister(source);
        this.removeReg(assigned);
        this.REGISTERS.get(index).add(assigned);
        // Lors d'une assignation directe, le registre de left est partagé avec 
        // assigned. Si left n'est plus une variable vive dans out, on peut
        // l'enlever de REGISTERS et le remplacer par assigned.
        this.MODIFIED.add(assigned);
        return null;
    }

    @Override
    public Object visit(ASTExpr node, Object data) {
        return node.jjtGetChild(0).jjtAccept(this, null);
    }

    @Override
    public Object visit(ASTIntValue node, Object data) {
        return "#"+String.valueOf(node.getValue());
    }

    @Override
    public Object visit(ASTIdentifier node, Object data) {
        return node.getValue();
    }


    /*
     * Fonction qui pourrait être utiles dans votre implémentation.
     * Vous n'êtes pas obligé de les complêter ou de les utiliser.
     */
    public String setReg(String src, int i) {
        // TODO : Met une variable "src" dans le registre "i". Retourne le nom du registre
        return "R" + i;
    }

    public String getReg(String src, int node, ArrayList<Vector<String>> maybe_dead) {
        // TODO 1: if exists, get existing register and return
        int associatedRegister = this.findAssociatedRegister(src);
        if(associatedRegister != -1) return "R"+associatedRegister;
        // TODO 2: if there is an empty register, put in empty register and return
        int nextEmptyRegister = this.findNextRegister();
        if(nextEmptyRegister != -1){
            this.REGISTERS.get(nextEmptyRegister).add(src);
            return "R"+ nextEmptyRegister;
        }
        // TODO 3: if there if dead variables in registers, put in dead register and return
        // TODO 4: other register selection (ex: put in oldest register and return)

        return ""; // default for compilation, should not be in your code!!
    }

    public void removeReg(String src) {
        // TODO : enlève une string du registers
        int index = this.findAssociatedRegister(src);
        if(index == -1) return;
        this.REGISTERS.get(index).remove(src);
        // Attention de voir s'il faut faire un ST ou non... (ST si c'est une variable vive)
    }

    private int findAssociatedRegister(String variable){
        for(int i=0; i <this.REGISTERS.size();i++){
            if(this.REGISTERS.get(i).contains(variable)) return i;
        }
        return -1;
    }

    private int findNextRegister(){
        for(int i=0; i <this.REGISTERS.size();i++){
            if(this.REGISTERS.get(i).size() == 0) return i;
        }
        return -1;
    }

    private void printLoad(String register, String variable){
        String toPrint = "LD "+ register + ", "+variable;
        m_writer.println(toPrint);
    }

    private void printOp(String op, String destination, String source1, String source2){
        this.updateUse(source2);
        this.updateUse(source1);
        this.USE_QUEUE.add(destination);

        String toPrint = this.OP.get(op)+" "+ destination + ", "+source1 + ", "+source2;
        m_writer.println(toPrint);
    }

    private void updateUse(String register){
        if(this.USE_QUEUE.contains(register))return;
        this.USE_QUEUE.add(register);
        String variable = this.getVariableFromRegister(register);
        this.printLoad(register,variable);
    }

    private String getVariableFromRegister(String register){
        int index= this.indexForRegister(register);
        if(this.REGISTERS.get(index).size() <1) return "";
        return this.REGISTERS.get(index).get(0);
    }

    private int indexForRegister(String register){
        return Integer.parseInt(register.substring(1));
    }

    private void printStore(String variable){
        int registerIndex = this.findAssociatedRegister(variable);
        String toPrint = "ST "+ variable + ", R"+registerIndex ;
        m_writer.println(toPrint);
    }

}
