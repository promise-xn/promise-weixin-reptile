package com.example.weixinapireptile.awt;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *  显示一张图片
 * @author zsq
 * @version 1.0
 * @date 2021/3/31 11:01
 */
public class MyImageShowFrame extends JFrame {


    public MyImageShowFrame()  {
        this.setSize(450, 450);
        this.setDefaultCloseOperation(2);
    }

    public void showImage(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len = 0;
        while ( (len = is.read(buff)) > -1 ) {
            os.write(buff, 0, len);
        }

        ImageIcon icon = new ImageIcon(os.toByteArray());
        JButton button = new JButton();
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setIcon(icon);
        button.setPreferredSize(new Dimension(400,400));
        this.setPreferredSize(new Dimension(icon.getIconWidth() - 1, icon.getIconHeight() - 1));
        this.getContentPane().add(button);
        this.setVisible(true);
    }

    public void close() {
        this.setVisible(false);
    }

}
