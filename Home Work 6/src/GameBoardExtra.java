
public class GameBoardExtra extends GameBoard {
    
    public GameBoardExtra(int height, int width) {
        super(height, width);
    }
    
    public String updateBugs () {
        Node destination, bugMove, bug;
        String output = "";
        
        for (int i = 0; i < listOfBugs.size(); i++) {
            StringBuilder sb = new StringBuilder();
            bug = listOfBugs.get(i);
            destination = breathFirst(bug);
            bugMove = destination;
            
            output = output.concat(String.format("Bug %s: %s %d", bug.getElement(), "?", destination.getDistance()));
            sb.append(String.format(")%d,%d( ", bugMove.getColumn(), bugMove.getRow()));
            while (bugMove.getParentInPath() != bug) {
                bugMove = bugMove.getParentInPath();
                sb.append(String.format(")%d,%d( ", bugMove.getColumn(), bugMove.getRow()));
            }
            sb.append(String.format(")%d,%d( ", bug.getColumn(), bug.getRow()));
            output = output.concat(sb.reverse().toString() + "\n");
            
            bugMove.setElement(bug.getElement());
            bug.setElement(" ");
            listOfBugs.remove(i);
            listOfBugs.add(i, bugMove);
            if (destination.getDistance() == 1) {
                setCode(3);
                return output;
            }
        }
        return output;
    }
    
    public Node dijkstra (Node Start) {
        return null;
    }
}
