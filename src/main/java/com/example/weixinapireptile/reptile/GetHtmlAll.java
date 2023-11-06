package com.example.weixinapireptile.reptile;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.UUID;

import org.jsoup.select.Elements;

import javax.imageio.ImageIO;

public class GetHtmlAll {

    // 指定文件保存路径为：D:\Download1\resource
    private static String path = "D:\\Download1\\resource/";
    private static String filenameTemp;

    public static void main (String[] args) throws IOException {
        System.out.println("开始爬取网页内容...");
        // 如果指定的路径不存在，则创建
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //爬取文字内容
        final String url = "https://mp.weixin.qq.com/s/SAjK_t2XFAiYRnCwzDHOdA";
        //爬取图片，上下的2个网站需要相同，确保爬的是同一个页面的内容
        Connection connection = Jsoup.connect("https://mp.weixin.qq.com/s/SAjK_t2XFAiYRnCwzDHOdA");
        try {
            //先获得的是整个页面的html标签页面,输出到控制台
            Document doc = Jsoup.connect(url).get();
            String doc2 = doc.html();
            System.out.println(doc);

            //通过标签，获取正文标题
            Elements tit = doc.select("h2");
            String title = tit.text();
            System.out.println("正文标题：");
            System.out.println("\n" + title + "\n");

            //获取正文内容<p>
            Elements ejbtEls = doc.select("p");
            // 获取
            Elements zhengwen = doc.select("html");
            //因为整片文章有多段p标签，所以进行拼接
            StringBuilder article1 = new StringBuilder();
            for (Element cle : zhengwen) {
                article1.append(cle.text());
                article1.append("\n");
            }
            String article = article1.toString();
            System.out.println("正文：");
            System.out.println("正 文 内 容-已 省 略，避 免-控 制 台-输 出-太 多 内 容");
            // System.out.println(article);

            // 创建文件，写入文件
            GetHtmlAll.createTxtfile(title);
            GetHtmlAll.writeTxtfile(article);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 获取图片代码
        try {
            Element document = connection.get();
            Elements imgs = document.select("img");
            System.out.println("共检测到下列图片URL：");
            System.out.println("开始下载");
            System.out.println("imgs数量---------"+imgs.size());
            System.out.println("imgs"+imgs.toString());
            // 遍历img标签并获得src的属性
            for (Element element : imgs) {
                //获取每个img标签URL "abs:"表示绝对路径
                String imgSrc = element.attr("abs:data-src");
                // 打印URL
                System.out.println("imgSrc------------"+imgSrc);
                if (StringUtils.isEmpty(imgSrc)){
                    continue;
                }
                //下载图片到本地
//                jsoup_html_in_pircture.downImages("E:/struct/img", imgSrc);
                String path = "D:/Download1/resource/image/";
                URL imageUrl = new URL(imgSrc);
                URLConnection urlConnection = imageUrl.openConnection();
                urlConnection.setConnectTimeout(5*1000);
                InputStream inputStream = urlConnection.getInputStream();
                BufferedImage image = ImageIO.read(inputStream);
                if (image==null){
                    continue;
                }
                UUID uuid = UUID.randomUUID();

                File outputFile = new File(path+uuid+".jpg");
                ImageIO.write(image, "jpg", outputFile);
                inputStream.close();
            }
            System.out.println("图片下载完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建文件
     *
     * @param name
     * @author 落魄的诗人
     */
    private static boolean createTxtfile (String name) throws IOException {
        boolean flag = false;
        filenameTemp = path + name +".txt";
        File filename = new File(filenameTemp);
        // if文件不存在，创建，如存在，则覆盖。
        if (!filename.exists()) {
            filename.createNewFile();
            flag = true;
        }
        return flag;
    }

    private static boolean writeTxtfile (String ejbt) throws IOException {

        // 先读取爬到的内容，然后进行写入操作
        boolean flag = false;
        String filein = ejbt + "\r\n";
        String temp = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            // 文件路径
            File file = new File(filenameTemp);
            // 将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();

            // 保存该文件原有的内容
            for (int j = 1; (temp = br.readLine()) != null; j++) {
                buf = buf.append(temp);
                // System.getProperty("line.separator");
                // 行与行之间的分隔符 相当于“\n”
                buf = buf.append(System.getProperty("line.separator"));
            }
            buf.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buf.toString().toCharArray());
            pw.flush();
            flag = true;
        } catch (IOException e1) {
            throw e1;
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
            System.out.println("执行完毕");
            System.out.println("文件已保存在：" + path + "\n");
        }
        return flag;
    }

    /**
     * 保存图片
     *
     * @param filePath
     * @param imgUrl
     * @author jiu
     */

    public static void downImages (String filePath, String imgUrl) {
        // 若指定文件夹没有，则先创建
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 截取图片文件名
        String fileName = imgUrl.substring(imgUrl.lastIndexOf('/') + 1, imgUrl.length());

        try {
            // 文件名里面可能有中文或者空格，所以这里要进行处理。但空格又会被URLEncoder转义为加号
            String urlTail = URLEncoder.encode(fileName, "UTF-8");
            // 因此要将加号转化为UTF-8格式的%20
            imgUrl = imgUrl.substring(0, imgUrl.lastIndexOf('/') + 1) + urlTail.replaceAll("\\+", "\\%20");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 写出的路径
        File file = new File(filePath + File.separator + fileName);

        try {
            // 获取图片URL
            URL url = new URL(imgUrl);
            // 获得连接
            URLConnection connection = url.openConnection();
            // 设置10秒的相应时间
            connection.setConnectTimeout(10 * 1000);
            // 获得输入流
            InputStream in = connection.getInputStream();
            // 获得输出流
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            // 构建缓冲区
            byte[] buf = new byte[1024];
            int size;
            // 写入到文件
            while (-1 != (size = in.read(buf))) {
                out.write(buf, 0, size);
            }
            out.close();
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

