package ability;

import com.sun.source.tree.NewArrayTree;

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
    private JPanel taskPanel;
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

    public TodoApp(){
        tasks = new ArrayList<Task>();
        dataload();
        startUI();
    }
    private void dataload(){

        try (BufferedReader datas  = new BufferedReader(new FileReader(FILE_PATH))){
            String data;

            while ((data=datas.readLine()) !=null){
                 String[] lines = data.split("\\|");
                 if(lines.length == 3) {
                     String title = lines[0];
                     boolean check = Boolean.parseBoolean(lines[1]);
                     String date = lines[2];

                     tasks.add(new Task(title, check, date));
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
        add(inputPanel,BorderLayout.NORTH);

        // 목록 패널
        taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel,BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(taskPanel);
        add(scrollPane,BorderLayout.CENTER);

        updateTaskList();

    }//startUI()

    private void addTask(){
      String inputText = taskInput.getText().trim();
      if(!inputText.isEmpty()){
          String currentTime = LocalDateTime.now()
                  .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
          tasks.add(new Task(inputText,false,currentTime));
          taskInput.setText("");
          updateTaskList();
          saveTasks();
      }else{
          JOptionPane.showMessageDialog(this,
                  "할일을 입력해주세요",
                  "경고",JOptionPane.WARNING_MESSAGE);
      }

    }//addTask()

    // 저장
    private void saveTasks(){
        try (PrintWriter saveDate = new PrintWriter(new FileWriter("tasks.txt"))){
           for(Task task : tasks){
              saveDate.println( task.title+"|" +
                                task.check+"|" +
                                task.date);
           }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void updateTaskList(){
        taskPanel.removeAll();
        for (int i = 0; i < tasks.size() ; i++) {
            Task task = tasks.get(i);
            JPanel taskRow = new JPanel(new BorderLayout());

            JLabel label = new JLabel(task.title +"("+task.date+")");
            if(task.check){
                label.setForeground(Color.GRAY);
            }
            taskRow.add(label,BorderLayout.CENTER);
            taskRow.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            taskPanel.add(taskRow);
        } //for문
        taskPanel.revalidate();
        taskPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TodoApp().setVisible(true);
        });
    }
}