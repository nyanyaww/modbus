package com.nyanyaww.Test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.*;

public class SwingTest {
    private JFrame frame = new JFrame("Server");

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

        sendMsg();
        getMsg();
    }

    private void sendMsg() {
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textAreaRight.getText() != null && !textAreaRight.getText().isEmpty()) {
                    textAreaLeft.setLayout(new FlowLayout(FlowLayout.RIGHT));
                    textAreaLeft.append("\nServer : \n    " + textAreaRight.getText());
                    scrollBottom();
                }
            }
        });
    }

    private void getMsg() {
        //TODO 接收消息
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

    public static void main(String[] args) {
        SwingTest swingTest = new SwingTest();
        swingTest.swing();
    }
}
