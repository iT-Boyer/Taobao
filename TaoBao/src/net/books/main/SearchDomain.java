package net.books.main;

import java.io.File;   
import java.io.FileOutputStream;   
import java.io.IOException;   
import java.io.InputStream;   
  
import org.apache.http.HttpEntity;   
import org.apache.http.HttpHost;   
import org.apache.http.HttpResponse;   
import org.apache.http.HttpStatus;   
import org.apache.http.client.ClientProtocolException;   
import org.apache.http.client.HttpClient;   
import org.apache.http.client.methods.HttpGet;   
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;   
  
public class SearchDomain {   
  
    public static void main(String[] args) throws ClientProtocolException, IOException {   
        //实例化一个HttpClient   
        HttpClient httpClient = new DefaultHttpClient();   
        //设定目标站点  web的默认端口80可以不写的 当然如果是其它端口就要标明                                                              
        HttpHost httpHost = new HttpHost("epaper.timedg.com",80);   
        //设置需要下载的文件   
        HttpGet httpGet = new HttpGet("/page/1/2011-08-10/A13/45531312906392100.pdf");  
        String agent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13";   
        httpGet.setHeader("User-Agent", agent);  
        httpGet.setHeader("referer","http://epaper.timedg.com/");
        //这里也可以直接使用httpGet的绝对地址，当然如果不是具体地址不要忘记/结尾   
        //HttpGet httpGet = new HttpGet("http://www.0431.la/");   
        //HttpResponse response = httpClient.execute(httpGet);   
           
        HttpResponse response = httpClient.execute(httpHost, httpGet);   
        if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode()){   
            //请求成功   
            //取得请求内容   
            HttpEntity entity = response.getEntity();   
               
            //显示内容   
            if (entity != null) {   
                //这里可以得到文件的类型 如image/jpg /zip /tiff 等等 但是发现并不是十分有效，有时明明后缀是.rar但是取到的是null，这点特别说明   
                System.out.println(entity.getContentType());   
                //可以判断是否是文件数据流   
                System.out.println(entity.isStreaming());   
                //设置本地保存的文件   
                File storeFile = new File("d:/pdf/bbb.pdf");     
                FileOutputStream output = new FileOutputStream(storeFile);   
                //得到网络资源并写入文件   
                InputStream input = entity.getContent();   
                byte b[] = new byte[1024];   
                int j = 0;   
                while( (j = input.read(b))!=-1){   
                    output.write(b,0,j);   
                }   
                output.flush();   
                output.close();    
            }   
            if (entity != null) {   
                entity.consumeContent();   
            }   
        }   
    }   
}  
