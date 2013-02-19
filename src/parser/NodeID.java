package parser;

/**
 * Класс предназначенный для хранения имен вершин и их ID
 * @author BoytsevAndrey
 */
public class NodeID {
    public String nameNode;
    public int IDNode;
    
    /**
     * Конструктор для создания нового парсера
     * @param name - Имя вершины
     * @param ID - ID вершины
     */
    public NodeID(String name, int ID){
        nameNode = name;
        IDNode = ID; 
    }    
}