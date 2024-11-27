package com.example.specialprojectreal;

import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class NavController implements Initializable {

    List<List<Node>> adj_list = new ArrayList<>();// adjacency list representation of graph (like 2D array)
    List<Double> rotations = new ArrayList<>();
    List<String> hallNames = new ArrayList<>();
    Stack<Node> prevNodes = new Stack<>();
    List children;

    int source; //starting room
    int rotationIndex = 0;
    int hallNameIndex = 0;
    boolean desReached = false;
    boolean newHall = true;
    int V = 4; //verticies
    Node src_node;
    Node destination_node;
    Text src_text;
    Rectangle rec = new Rectangle();
    ImageView image = new ImageView(new Image("C:\\Users\\wusua\\OneDrive\\Documents\\GitHub\\Navigation-System\\src\\img\\arrow.png"));
    PathTransition transition = new PathTransition();
    Path path = new Path();


    @FXML
    ImageView imgMap;

    @FXML
    Text directionsText, directionsText1, alertTxt;

    @FXML
    Button directionsBtn, nextDirectionBtn;

    @FXML
    AnchorPane leftPane, rightPane, linePane;

    @FXML
    ListView<String> startingPoints;

    @FXML
    ListView<String> destinationPoints;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Initialize adjacency list for every node in the graph
        for (int i = 0; i < V; i++) {
            List<Node> item = new ArrayList<Node>();
            adj_list.add(item);
        }
        String[] roomNames = {
                "Main", "One Stop", "Hall-200", "Restroom-200", "Room-223", "Room-225", "Room-227"
                , "Room-224", "Room-226", "Room-228", "Room-225"
        };

        rec.setHeight(10);
        rec.setWidth(10);
        rec.setFill(Color.RED);
        rec.setOpacity(0);
        children = List.of(linePane.getChildren().toArray());

        // Input graph edges
        adj_list.get(0).add(new Node(1, 16, "One Stop")); //Main to One Stop
        adj_list.get(0).add(new Node(2, 4, "Hall-200")); //Main to Hall 200
        adj_list.get(2).add(new Node(3, 2, "Restroom-200")); //Hall 200 to Restroom 200
        adj_list.get(2).add(new Node(0, 4, "Main")); //Hall 200 to Main
        adj_list.get(3).add(new Node(2, 2, "Hall-200")); //Restroom 200 to Hall 200
        adj_list.get(2).add(new Node(1, 4, "One Stop")); //Hall 200 to One Stop


        calculateDistances(0, "Main");
        for (String roomName : roomNames) { //Sets List View Data
            destinationPoints.getItems().add(roomName);
            startingPoints.getItems().add(roomName);
        }
        alertTxt.setText("Current Starting Point: Main");
    }

    void calculateDistances(int num, String name){
        source = num; //Main entrance
        src_node = new Node(source, 0, name);

        // call Dijkstra's algo method
        Graph_dq dpq = new Graph_dq(V);
        dpq.algo_dijkstra(adj_list, source,src_node);

        // Print the shortest path from source node to all the nodes
        System.out.println("The shorted path from source node to other nodes:");
        System.out.println("Source\t\t" + "Node#\t\t" + "Distance\t\t" + "Previous");
        for (int i = 0; i < dpq.dist.length; i++){
            System.out.print(source + " \t\t " + i + " \t\t "  + dpq.dist[i] + "\t\t");
            for (List<Node> nodes : adj_list) {
                Node v = new Node(i, 0);
                int inde = nodes.indexOf(v);
                if (inde != -1) {
                    v = nodes.get(inde);
                    if (v.getListIndex() != -1) {
                        System.out.print("\t\t\t" + adj_list.get(v.getListIndex()).get(v.getNodeIndex()).getName());
                    }
                    break;
                }
            }
            System.out.println();
        }
    }

    @FXML
    void onSourceBtn(MouseEvent e){ //Sets Source depending on what button clicked
        if(e.getSource().getClass() == Button.class){
            Button btn = (Button) e.getSource();

            if(btn.getText().equals("Main")){
                source = 0;
                calculateDistances(source, "Main");
                alertTxt.setText("Current Starting Point: Main");
            } else if(btn.getText().equals("Exit/Entrance-800")){
                source = 9;
                calculateDistances(source, "Exit/Entrance-800");
                alertTxt.setText("Current Starting Point: Exit/Entrance-800");
            } else if(btn.getText().equals("Room-604-Police")){
                source = 28;
                calculateDistances(source, "Room-604-Police");
                alertTxt.setText("Current Starting Point: Room-604-Police");
            }
        }
    }

    @FXML
    protected void onSourceSelectionModel(){ //Sets source depending on which data in the list view is clicked
        String name = "";
        for(int i = 0; i < adj_list.size(); i++){ //Loops through adj list finds room name and sets source and name
            for(int j = 0; j < adj_list.get(i).size(); j++){
                if(startingPoints.getSelectionModel().getSelectedItem().equals(adj_list.get(i).get(j).getName())){
                    source = adj_list.get(i).get(j).getNode();
                    name = adj_list.get(i).get(j).getName();
                    break;
                }
            }
        }
        alertTxt.setText("Current Starting Point: "+name);
        calculateDistances(source, name); //Calculate distance to every room from source
    }

    @FXML
    int onDestinationEnter(MouseEvent e){ //Finds the destination load in the list
        String destination;

        if(e.getSource().getClass() == Button.class){ //If button was clicked
            Button btn = (Button) e.getSource();
            destination = btn.getText();
        } else { //If list view data was clicked
            destination = destinationPoints.getSelectionModel().getSelectedItem();
        }

        if(destination.equals(src_node.getName())){ //If destination is the same as source name
            return -1;
        }

            destination_node = new Node();
            for(int i = 0; i < adj_list.size(); i++){
                for (int j = 0; j < adj_list.get(i).size(); j++){
                    if(adj_list.get(i).get(j).getName().equals(destination)){ //If Room Destination Name equals a room in adj List
                        destination_node = adj_list.get(i).get(j); //Assign Destination Node
                        break;
                    }
                }
            }
            leftPane.setVisible(false);
            rightPane.setVisible(false);
            addNodesToStack(destination_node); //Add Previous Nodes to Stack
            //Reset Variables
            nextDirectionBtn.setVisible(true);
            rotationIndex = 0;
            hallNameIndex = 0;
            desReached = false;
            newHall = true;
            rotations = new ArrayList<>();
            hallNames = new ArrayList<>();
            src_text = getSrcTxt();
            path = new Path();
            path.getElements().add(new MoveTo(getSrcTxt().getLayoutX(), getSrcTxt().getLayoutY()));
            linePane.getChildren().add(image);
            onNextDirectionClick();
        return 0;
    }

    void addNodesToStack(Node desNode){ //Adds Previous Nodes to stack
        Node prev = adj_list.get(desNode.listIndex).get(desNode.nodeIndex);

        prevNodes.push(desNode); //Add destination node to stack
        if(!prev.getName().equals(src_node.name)){ //Make Sure the first previous isn't equal to source name
            prevNodes.push(prev); //Add First Previous Node
        }
        desNode = prev;
        if(desNode.listIndex != -1){ //If List index not equal -1
            prev = adj_list.get(desNode.listIndex).get(desNode.nodeIndex);
        }
        while(prev.getNode() != source){ //Loop and add prev nodes until it reaches source node
            if(prev.getName().startsWith("Hall")){
                prevNodes.push(prev);
            }
            desNode = prev;
            prev = adj_list.get(desNode.listIndex).get(desNode.nodeIndex);}
    }

     void setLines(Stack<Node> prevNodes){ //Sets paths and determines rotation of lines to know whether to turn right or left
         String text = "";
         Node tempNode = new Node(0, 0, "");
         int counter = 0;
         transition = new PathTransition();
         Text tempTxt = new Text("");

         while(!prevNodes.isEmpty() && counter < 2){
             tempNode = prevNodes.pop();
             for(int i = 0;  i < children.size(); i++){
                 if(children.get(i).getClass() == Text.class){
                     tempTxt = (Text) children.get(i);
                     if(tempNode.getName().equals(tempTxt.getText())){

                         Line line = new Line();
                        line.setStartX(src_text.getLayoutX()); //Starts at the source node
                        line.setStartY(src_text.getLayoutY()); //Starts at source Node
                        line.setEndX(tempTxt.getLayoutX()); //Ends at button node
                        line.setEndY(tempTxt.getLayoutY()); //Ends at button Node

                         double x = line.getStartX() - line.getEndX(); //Get X Point of Line
                         double y = line.getStartY() - line.getEndY(); //Get Y point of Line
                          double rotation = (Math.toDegrees(Math.atan2(x, y))); //Using Math.atan2 gets the angle measure between axis

                         if(tempNode.getName().startsWith("Hall") && !tempNode.getName().equals(destination_node.getName())){
                             hallNames.add(tempNode.getName()); //Add Halls
                         }

                         rotations.add(rotation);
                         System.out.println(rotation);

                         src_text = tempTxt;
                         path.getElements().add(new LineTo(tempTxt.getLayoutX(), tempTxt.getLayoutY())); //Make a new path
                         counter++;
                     }
                 }
             }
         }
         rec.setOpacity(1);
         transition.setNode(image); //Set node that will be following path
         transition.setDuration(Duration.seconds(3));
         transition.setCycleCount(5);
         transition.setPath(path); //Follows Line
         transition.play();
     }

     @FXML
     protected void onNextDirectionClick(){ //Event Fired when user wants to see next directions
         setLines(prevNodes); //Set Path Lines
        String text = "";
         if(hallNameIndex != hallNames.size()){ //If we haven't shown all directions to user then set directions and show
             setDirections(rotations, hallNames);
         } else if (desReached){
             onReachedDestination();
             linePane.getChildren().remove(image);
         } else {
             desReached = true;
             text += "go to " + destination_node.getName() + ", you have reached your destination!";
             directionsText.setText(text);
         }
     }

     void setDirections(List<Double> rotations, List<String> hallNames){ //Using rotations it determines if user should go right left or forward
         String text = "";
         int currRotationIndex = 0;
         double currRotation = 0, followingRotation = 0;
         if(!rotations.isEmpty()){
             currRotation = rotations.get(rotationIndex); //Determines Compass Position
             followingRotation = rotations.get(rotationIndex); //Uses Compass Position to determine left or right
         }

         while(hallNameIndex != hallNames.size() && rotations.size() >= 1){ //Loops through rotations array and determines left or right
             rotationIndex++;

             if(currRotation > -5 && currRotation < 21){ //If Compass Position is facing forward
                 if(followingRotation < -80 && followingRotation > -100){
                     text += "turn right, ";
                 } else if(followingRotation > 80 && followingRotation < 100){
                     text += "turn left, ";
                 }  if (newHall){
                     newHall = false;
                     text += "Go towards " + hallNames.get(hallNameIndex) + ", ";
                 }
             }

             if(currRotation < -80 && currRotation > -100){ //If Compass Position is facing right
                 if(followingRotation < -140 && followingRotation > -185 || followingRotation > 150 && followingRotation < 185 ){
                     text += "turn right, ";
                 } else if (followingRotation > -9 && followingRotation < 10){
                     text += "turn left, ";
                 } else if (newHall){
                     newHall = false;
                     text += "Go towards " + hallNames.get(hallNameIndex) + ", ";
                 }
             }

             if(currRotation > 80 && currRotation < 105){ //If Compass Position is facing left
                 if(followingRotation > -5 && followingRotation < 25){
                     text += "turn right, ";
                 } else if (followingRotation > 130 && followingRotation < 190 || followingRotation < -130 && followingRotation > -190){
                     text += "turn left, ";
                 } else if (newHall){
                     newHall = false;
                     text += "Go towards " + hallNames.get(hallNameIndex) + ", ";
                 }
             }

             if(currRotation < -144 && currRotation > -190 || currRotation > 155 && currRotation < 190){ //If Compass Position is facing down
                 if(followingRotation > 80 && followingRotation < 105){
                     text += "turn right, ";
                 } else if (followingRotation < -80 && followingRotation > -105){
                     text += "turn left, ";
                 } else if (newHall){
                     newHall = false;
                     text += "Go towards " + hallNames.get(hallNameIndex) + ", ";
                 }
             }

             if(rotationIndex == 2){
                 newHall = true; //Reached a new hall
                 rotations.remove(currRotationIndex); //Removes Curr Rotation or Compass and moves to next
                 rotationIndex = 0;
                 hallNameIndex++;
                 if(!rotations.isEmpty()){
                     currRotation = rotations.get(currRotationIndex);
                 }
                 break;
             }
             if(rotationIndex != rotations.size()){
                 followingRotation = rotations.get(rotationIndex);
             }
         }

         directionsText.setText(text);
         directionsText.setVisible(true);
     }


    Text getSrcTxt(){ //Get Src node button to use position
        for(int i = 0; i < children.size(); i++){
            if(children.get(i).getClass() == Text.class){
                Text tempTxt = (Text) children.get(i);
                if(src_node.getName().equals(tempTxt.getText())){
                    return tempTxt;
                }
            }
        }
        return new Text();
    }

     void onReachedDestination(){ //When destination is reached
        source = destination_node.getNode(); //Sets source to the destination reached
        calculateDistances(source, destination_node.getName());
        alertTxt.setText("Current Starting Point: "+destination_node.getName());
        leftPane.setVisible(true);
        rightPane.setVisible(true);
        imgMap.setVisible(true);
        nextDirectionBtn.setVisible(false);
        directionsText.setVisible(false);
        directionsText.setText("");

    }

}

/*
0 - Main entrance
1 - One stop
2 - Restroom
3 - Room102
4 - 104
5 - 106
6 - 107
7 - Hall100x800
8 - 802
9 - Exit/Entrance 800
10 - 804
11 - 801
12 - 803
13 - 805
14 - 808
15 - 810
16 - 807
17 - 812
18 - 814
19 - Restroom800
20 - 813
21 - Hall 800x500
22 - 529
23 - Hall 500x650
24 - 528
25 - 527
26 - 606
27 - Hall 500x600
28 - 604
29 - 605
30 - Hall 600x300
31 - 601
32 - Hall 100
33 - 323
34 - 321
35 - 319
36 - 315
 */


