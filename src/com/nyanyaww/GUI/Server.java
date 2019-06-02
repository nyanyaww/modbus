package com.nyanyaww.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author nyanyaww
 * @program modbus
 * @description
 * @create 2019-05-30 23:18
 **/
public class Server {
    private JPanel mainPanel;
    private JPanel top;
    private JPanel center;

    public Server() {
        okButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == okButton) {
                    System.out.println("ok");
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Server");
        frame.setContentPane(new Server().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private JPanel bottom;
    private JComboBox comboBox1;
    private JButton okButton;
    private JTextArea textArea1;

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(50, 50));
        mainPanel.setMinimumSize(new Dimension(800, 600));
        mainPanel.setPreferredSize(new Dimension(800, 600));
        top = new JPanel();
        top.setLayout(new GridBagLayout());
        mainPanel.add(top, BorderLayout.NORTH);
        final JLabel label1 = new JLabel();
        label1.setRequestFocusEnabled(true);
        label1.setText("一份小小的使用帮助");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 500;
        top.add(label1, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        top.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        top.add(spacer2, gbc);
        center = new JPanel();
        center.setLayout(new GridBagLayout());
        mainPanel.add(center, BorderLayout.CENTER);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 50;
        center.add(spacer3, gbc);
        textArea1 = new JTextArea();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipadx = 600;
        gbc.ipady = 500;
        center.add(textArea1, gbc);
        bottom = new JPanel();
        bottom.setLayout(new GridBagLayout());
        mainPanel.add(bottom, BorderLayout.SOUTH);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        bottom.add(spacer4, gbc);
        comboBox1 = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("读取线圈的值");
        comboBox1.setModel(defaultComboBoxModel1);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bottom.add(comboBox1, gbc);
        okButton = new JButton();
        okButton.setText("确认");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bottom.add(okButton, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}