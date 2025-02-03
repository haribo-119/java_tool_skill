package ability;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TodoApp extends JFrame {
    private ArrayList<Task> tasks;
    private JTextField taskInput;
    private static final String FILE_PATH ="tasks.txt";

    public static class Task{
        String title;
        boolean check;
        String date;

        public Task(String title, boolean check, String date) {
            this.title = title;
            this.check = check;
            this.date = date;
        }
    }

    public void TodoApp(){
        tasks = new ArrayList<Task>();
        dataload();
        startUI();
    }
    private void dataload(){

        try{
            BufferedReader datas  = new BufferedReader(new FileReader("tasks.txt"));
            String data;

            while ((data=datas.readLine()) !=null){
                 String[] lines = data.split("\\|");
                 if(data.length()==3) {
                     String tilte = lines[0];
                     boolean check = Boolean.parseBoolean(lines[1]);
                     String date = lines[3];

                     tasks.add(new Task(tilte, check, date));
                 }
            }

        }catch (Exception e){
            // 파일이 없는 경우 무시
        }
    }//dataload()

    private void startUI(){
        setTitle("to-do list");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,600);
        setLayout(new BorderLayout());

        //입력 패널
        JPanel inputPanel = new JPanel();
        taskInput = new JTextField(20);
        JButton addButton = new JButton("추가");
        addButton.addActionListener(e -> addTask());

        inputPanel.add(taskInput);
        inputPanel.add(addButton);
    }//startUI()

    private void addTask(){
      String inputText = taskInput.getText().trim();
      if(inputText.isEmpty()){
          String currentTime = LocalDateTime.now()
                  .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
          tasks.add(new Task(inputText,false,currentTime));
          taskInput.setText("");
      }else{
          JOptionPane.showConfirmDialog(this,
                  "할일을 입력해주세요",
                  "경고",JOptionPane.WARNING_MESSAGE);
      }

    }//addTask()


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TodoApp().setVisible(true);
        });
    }
}