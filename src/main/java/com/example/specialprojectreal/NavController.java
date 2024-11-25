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
    int V = 37; //verticies
    Node src_node;
    Node destination_node;
    Text src_text;
    Rectangle rec = new Rectangle();
    ImageView image = new ImageView(new Image("C:\\Users\\wusua\\OneDrive\\Documents\\GitHub\\Navigation-System\\src\\img\\Web_capture_1-11-2023_94011_.jpeg"));
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
                "Main", "One Stop", "Hall-100", "Restroom-100", "Room-102", "Room-104", "Room-106", "Room-107",
                "Hall 100x800", "Room-802", "Exit/Entrance-800", "Room-804", "Room-801", "Room-803",
                "Room-805", "Room-808", "Room-810", "Room-807", "Room-812", "Room-814", "Restroom-800",
                "Room-813", "Hall 800x500", "Room-529", "Hall 500x650", "Room-528", "Room-527", "Student-Lounge-606",
                "Hall 500x600", "Room-604-Police", "Room-605", "Hall 600x300", "Room-601","Room-315","Room-319","Room-321","Room-323"
        };

        rec.setHeight(10);
        rec.setWidth(10);
        rec.setFill(Color.RED);
        rec.setOpacity(0);
        children = List.of(linePane.getChildren().toArray());

        // Input graph edges
        adj_list.get(0).add(new Node(1, 16, "One Stop")); //Main to One Stop
        adj_list.get(0).add(new Node(32, 4, "Hall-100")); //Main to Hall 100
        adj_list.get(32).add(new Node(2, 2, "Restroom-100")); //Hall 100 to Restroom 100
        adj_list.get(32).add(new Node(0, 4, "Main")); //Hall 100 to Main


        adj_list.get(1).add(new Node(0, 16, "Main")); //One Stop to Main

        adj_list.get(2).add(new Node(3, 8, "Room-102")); //Restroom to Room 102
        adj_list.get(2).add(new Node(32, 2, "Hall-100")); //Restroom to Hall 100

        adj_list.get(3).add(new Node(2, 8, "Restroom-100")); //Room 102 to Restroom 100

        adj_list.get(3).add(new Node(4, 9, "Room-104")); //Room 102 to 104
        adj_list.get(4).add(new Node(3, 9, "Room-102")); //Room 104 to 102

        adj_list.get(4).add(new Node(5, 1, "Room-106")); //Room 104 to 106
        adj_list.get(5).add(new Node(4, 1, "Room-104")); //Room 106 to 104

        adj_list.get(5).add(new Node(6, 10, "Room-107")); //Room 106 to 107
        adj_list.get(6).add(new Node(5, 10, "Room-106")); //Room 107 to 106

        adj_list.get(5).add(new Node(7, 4, "Hall 100x800")); //Room 106 to Hall 100x800
        adj_list.get(7).add(new Node(5, 4, "Room-106")); //Hall 100x800 to Room 106

        adj_list.get(7).add(new Node(8, 8, "Room-802")); //Hall 100x800 to 802
        adj_list.get(8).add(new Node(7, 8, "Hall 100x800")); //Hall 802 to 100x800

        adj_list.get(8).add(new Node(9, 1, "Exit/Entrance-800")); //Room 802 to Exit800
        adj_list.get(9).add(new Node(8, 1, "Room-802")); //Room Exit800 to Room 802

        adj_list.get(7).add(new Node(10, 5, "Room-804")); // 100800 to 804
        adj_list.get(10).add(new Node(7, 5, "Hall 100x800")); // 804 to 100800

        adj_list.get(10).add(new Node(11, 5, "Room-801")); //Room 804 to 801
        adj_list.get(11).add(new Node(10, 5, "Room-804")); //Room 801 to 804

        adj_list.get(11).add(new Node(12, 6, "Room-803")); //Room 801 to 803
        adj_list.get(12).add(new Node(11, 6, "Room-801")); //Room 803 to 801

        adj_list.get(12).add(new Node(13, 1, "Room-805")); //Room 803 to 805
        adj_list.get(13).add(new Node(12, 1, "Room-803")); //Room 805 to 803

        adj_list.get(13).add(new Node(14, 5, "Room-808")); //Room 805 to 808
        adj_list.get(14).add(new Node(13, 5, "Room-805")); //Room 808 to 805

        adj_list.get(14).add(new Node(15, 1, "Room-810")); //Room 808 to 810
        adj_list.get(15).add(new Node(14, 1, "Room-808")); //Room 810 to 808

        adj_list.get(15).add(new Node(16, 2, "Room-807")); //Room 810 to 807
        adj_list.get(16).add(new Node(15, 2, "Room-810")); //Room 807 to 810

        adj_list.get(16).add(new Node(17, 2, "Room-812")); //Room 807 to 812
        adj_list.get(17).add(new Node(16, 2, "Room-807")); //Room 812 to 807

        adj_list.get(17).add(new Node(18, 1, "Room-814")); //Room 812 to 814
        adj_list.get(18).add(new Node(17, 1, "Room-812")); //Room 814 to 812

        adj_list.get(18).add(new Node(19, 3, "Restroom-800")); //Room 814 to Restroom 800
        adj_list.get(19).add(new Node(18, 3, "Room-814")); //Restroom 800 to Room 814

        adj_list.get(19).add(new Node(20, 2, "Room-813")); //Restroom 800 to 813
        adj_list.get(20).add(new Node(19, 2, "Restroom-800")); //813 to Restroom 800

        adj_list.get(20).add(new Node(21, 3, "Hall 800x500")); //Room 813 to Hall 800x500
        adj_list.get(21).add(new Node(20, 3, "Room-813")); //Hall 800x500 to Room 813

        adj_list.get(21).add(new Node(22, 20, "Room-529")); //Hall 800x500 to Room 529
        adj_list.get(22).add(new Node(21, 20, "Hall 800x500")); //Room 529 to Hall 800x500

        adj_list.get(22).add(new Node(23, 3, "Hall 500x650")); //Room 529 TO Hall 500x650
        adj_list.get(23).add(new Node(22, 3, "Room-529")); //Hall 500x650 to Room 529

        adj_list.get(23).add(new Node(24, 3, "Room-528")); //Hall 500x650 to 528
        adj_list.get(24).add(new Node(23, 3, "Hall 500x650")); //528 to Hall 500x650

        adj_list.get(24).add(new Node(25, 3, "Room-527")); //Room 528 to 527
        adj_list.get(25).add(new Node(24, 3, "Room-528")); //Room 527 to 528

        adj_list.get(25).add(new Node(26, 6, "Room-606")); //Room 527 to 606
        adj_list.get(26).add(new Node(25, 6, "Room-527")); //Room 606 to 527

        adj_list.get(26).add(new Node(27, 10, "Hall 500x600")); //Room 606(Student Lounge to Hall 500x600
        adj_list.get(27).add(new Node(26, 10, "Student-Lounge-606")); //Hall 500x600 to Room 606

        adj_list.get(27).add(new Node(28, 9, "Room-604-Police")); //Hall 500x600 to 604
        adj_list.get(28).add(new Node(27, 9, "Hall 500x600")); //604 to Hall 500x600

        adj_list.get(28).add(new Node(29, 2, "Room-605")); //Room 604 to 605
        adj_list.get(29).add(new Node(28, 2, "Room-604-Police")); //Room 605 to 604

        adj_list.get(29).add(new Node(30, 17, "Hall 600x300")); //Room 605 to Hall 600x300
        adj_list.get(30).add(new Node(29, 17, "Room-605")); //Hall 600x300 to Room 605

        adj_list.get(30).add(new Node(31, 6, "Room-601")); //Hall 600x300 to Room 601
        adj_list.get(31).add(new Node(30, 6, "Hall 600x300")); //Room 601 to Hall 600x300

        adj_list.get(30).add(new Node(1,1, "One Stop")); //Hall600x300 to One Stop
        adj_list.get(1).add(new Node(30,1, "Hall 600x300")); //One Stop to Hall600x300

        adj_list.get(31).add(new Node(0,22, "Main")); //Room 601 to Main
        adj_list.get(0).add(new Node(31,22, "Room-601")); //Main to Room 601

        adj_list.get(33).add(new Node(30, 2, "Hall 600x300")); //Room 323 to Hall 600x300
        adj_list.get(30).add(new Node(33, 2, "Room-323")); //Hall 600x300 to Room 323

        adj_list.get(34).add(new Node(33, 5, "Room-323")); //Room 321 to Room 323
        adj_list.get(33).add(new Node(34, 5, "Room-321")); //Room 323 to 321

        adj_list.get(35).add(new Node(34, 2, "Room-321")); //Room 319 to Room 321
        adj_list.get(34).add(new Node(35, 2, "Room-319")); //Room 321 to 319

        adj_list.get(36).add(new Node(35, 1, "Room-319")); //Room 315 to 319
        adj_list.get(35).add(new Node(36, 1, "Room-315")); //Room 319 to 315

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


