package com.nyanyaww.Test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.*;

public class ClientSwingDemo {
    private PrintWriter pwtoserver = null;

    private Socket socket = null;

    private JFrame frame = new JFrame("Client");

    private JTextArea textAreaLeft = new JTextArea(5, 10);

    private JTextArea textAreaRight = new JTextArea(5, 10);

    private JScrollPane scrollPane = new JScrollPane(textAreaLeft);

    private JSplitPane topSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, scrollPane, textAreaRight);

    private JPanel panel = new JPanel();

    private JButton button = new JButton("发送");

    private JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, topSplitPane, panel);


    private void swing() {

        textAreaLeft.setEnabled(false);
        textAreaRight.addKeyListener(new MyListener());

        topSplitPane.setDividerLocation(160);
        topSplitPane.setDividerSize(2);

        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.setSize(600, 20);
        panel.add(button);

        splitPane.setDividerLocation(230);
        splitPane.setDividerSize(2);

        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(splitPane);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void sendMsg() {
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textAreaRight.getText() != null && !textAreaRight.getText().isEmpty()) {
                    textAreaLeft.setLayout(new FlowLayout(FlowLayout.RIGHT));

                    String msg = "\nClient : \n    " + textAreaRight.getText();
                    textAreaLeft.append(msg);
                    pwtoserver.println(msg);
                    pwtoserver.flush();
                    textAreaRight.setText("");
                    scrollBottom();
                }
            }
        });
    }

    private void getMsg() {
        try {
            Scanner inScanner = new Scanner(socket.getInputStream());
            while (inScanner.hasNextLine()) {
                String indata = inScanner.nextLine();
                if (!indata.isEmpty()) {
                    textAreaLeft.append("\n" + indata);
                    scrollBottom();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scrollBottom() {
        textAreaLeft.setSelectionStart(textAreaLeft.getText().length());
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    class MyListener implements KeyListener {
        @Override // 按下
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 10) {
                button.doClick();
            }
        }

        @Override // 松开
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == 10) {
                textAreaRight.setText("");
            }
        }

        @Override // 输入的内容
        public void keyTyped(KeyEvent e) {
        }
    }

    private void server() {
        swing();

        textAreaLeft.setText("Requesting a connection to the server");

        try {
            System.out.println();
            socket = new Socket(InetAddress.getLocalHost().getHostAddress(), 6666);
            pwtoserver = new PrintWriter(socket.getOutputStream());
            pwtoserver.flush();

            sendMsg();
            getMsg();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pwtoserver.close();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ClientSwingDemo clientSwingDemo = new ClientSwingDemo();
        clientSwingDemo.server();
    }

}
