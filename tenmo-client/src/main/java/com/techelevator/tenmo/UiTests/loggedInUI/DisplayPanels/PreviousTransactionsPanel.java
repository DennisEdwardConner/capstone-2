package com.techelevator.tenmo.UiTests.loggedInUI.DisplayPanels;

import com.techelevator.tenmo.UiTests.MyButton;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

public class PreviousTransactionsPanel extends JPanel implements ActionListener {

    private Color bgColor = new Color(0, 153, 102).darker();

    private JScrollPane transferListScrollPane;
    private JTextField userIdTextField = new JTextField();
    private JTextField amountTextField = new JTextField();

    private AuthenticatedUser currentUser;
    private TransferService transferService = new TransferService("http://localhost:8080/");
    private UserService userService = new UserService("http://localhost:8080/");
    private AccountService accountService = new AccountService("http://localhost:8080/");

    private JPanel homePanel;
    private Transfer selectedTransaction;

    public PreviousTransactionsPanel(AuthenticatedUser currentUser, JPanel homePanel){
        setBounds(10, 210, 370, 295);
        setBackground(bgColor);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        this.currentUser = currentUser;
        userService.setCurrentUser(currentUser);
        transferService.setCurrentUser(currentUser);
        accountService.setCurrentUser(currentUser);

        setUpScrollArea();
        setUpInputFields();

        this.homePanel = homePanel;

        setVisible(false);
    }

    private void setUpInputFields(){

        BufferedImage historyPanelBgImg = null;
        try {
            historyPanelBgImg = ImageIO.read(new File("Resources/HistoryPanel.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedImage finalHistoryPanelBgImg = historyPanelBgImg;
        JPanel inputFieldPanel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                Graphics2D g2D = (Graphics2D) g;

                g2D.drawImage(finalHistoryPanelBgImg, 0, 0, 220, 295, this);


                if(transferService.getPreviousTransfers().length == 0){
                    return;
                }else if(selectedTransaction == null){
                    selectedTransaction = transferService.getPreviousTransfers()[0];
                }

                //DRAW TEXT
                g2D.setColor(Color.WHITE);
                g2D.setFont(new Font(Font.SERIF, Font.BOLD, 30));
                g2D.drawString(String.valueOf(selectedTransaction.getTransfer_id()), 65, 45);
                g2D.drawString(selectedTransaction.getUsername_from(), 110, 85);
                g2D.drawString(selectedTransaction.getUsername_to(), 60, 125);
                g2D.drawString(selectedTransaction.getTransfer_type(), 95, 167);
                g2D.setFont(new Font(Font.SERIF, Font.BOLD, 24));

                if(selectedTransaction.getTransfer_status().equals("Approved"))
                    g2D.setColor(new Color(174, 245, 176));
                else if(selectedTransaction.getTransfer_status().equals("Rejected"))
                    g2D.setColor(new Color(255, 174, 174));
                g2D.drawString(selectedTransaction.getTransfer_status(), 115, 213);

                g2D.setColor(Color.WHITE);
                g2D.setFont(new Font(Font.SERIF, Font.BOLD, 20));
                g2D.drawString(selectedTransaction.getAmount().toString(), 135, 255);
            }
        };
        inputFieldPanel.setLayout(null);
        inputFieldPanel.setPreferredSize(new Dimension(220, 295));
        inputFieldPanel.setBackground(bgColor.darker());
        inputFieldPanel.setBorder(null);

        add(inputFieldPanel, BorderLayout.EAST);
    }


    private void setUpScrollArea(){
        JPanel transferPane = new JPanel();
        transferPane.setPreferredSize(new Dimension(150, 295));
        transferPane.setLayout(null);
        transferPane.setBackground(bgColor);

        Transfer[] transferList = transferService.getPreviousTransfers();

        int newY = 0;
        for(Transfer transfer : transferList){
            MyButton transferButton = new MyButton(String.valueOf(transfer.getTransfer_id()));
            transferButton.setBackground(bgColor);
            transferButton.setForeground(Color.yellow.darker());
            transferButton.setHoverBackgroundColor(bgColor.darker());
            transferButton.setFont(new Font(Font.SERIF, Font.BOLD, 20));
            transferButton.setBounds(0, newY, 150, 30);
            transferButton.setFocusable(false);
            transferButton.setBorderPainted(false);
            transferButton.setFocusPainted(false);
            transferButton.addActionListener(this);
            transferButton.setPressedBackgroundColor(new Color(14, 163, 63));
            transferButton.setActionCommand(String.valueOf(transfer.getTransfer_id()));
            newY += 30;

            transferPane.add(transferButton);
        }
        transferPane.setPreferredSize(new Dimension(150, newY));

        transferListScrollPane = new JScrollPane(transferPane);
        transferListScrollPane.setBorder(null);
        transferListScrollPane.setPreferredSize(new Dimension(150, 295));
        transferListScrollPane.remove(transferListScrollPane.getVerticalScrollBar());
        transferListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(transferListScrollPane, BorderLayout.WEST);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Transfer[] transferList = transferService.getPreviousTransfers();

        for(int i = 0; i < transferList.length; i++){
            if(e.getActionCommand().equals(String.valueOf(transferList[i].getTransfer_id()))){
                selectedTransaction = transferList[i];
            }
        }

        homePanel.repaint();
        repaint();
    }
}