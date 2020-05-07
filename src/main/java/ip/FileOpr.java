package ip;

import java.io.*;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*import proto.HttpParser;*/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.archivers.*;

public class FileOpr {
	
	private static final Log LOG = LogFactory.getLog(FileOpr.class);	
	
	public static void add_zip_dir(File file, int root_dir_len, ArchiveOutputStream os) throws Exception
	{	
		if (file.isDirectory()) {
			File[] array = file.listFiles();
			for (int i = 0; i < array.length; i++) 
			{
				add_zip_dir(array[i], root_dir_len, os);
			}
			
			String filename = file.getAbsolutePath();		
			if(filename.length() > root_dir_len)
			{
				filename = filename.substring(root_dir_len);
				
				os.putArchiveEntry(new ZipArchiveEntry(filename+"/"));				
				os.closeArchiveEntry();	
			}
			
		} else {	
			String filename = file.getAbsolutePath();			
			filename = filename.substring(root_dir_len);
			
			os.putArchiveEntry(new ZipArchiveEntry(filename));
			IOUtils.copy(new FileInputStream(file), os);
			os.closeArchiveEntry();					
		}
	}
	
	public static boolean compress_dir(String zip_filename, String dir)
	{		
		try
		{
			File output = new File(zip_filename);
			File f_dir = new File(dir);
			 
			OutputStream out = new FileOutputStream(output);
			ArchiveOutputStream os = new ArchiveStreamFactory().createArchiveOutputStream("zip", out);
			
			add_zip_dir(f_dir, f_dir.getAbsolutePath().length() + 1, os);
			
			os.close();
			out.close();
			
			os = null;
			out = null;
			
			return true;
		}
		catch(Exception e)
		{			
			e.printStackTrace();
		}			
		
		return false;
	}
	
	public static boolean uncompress_dir(String zip_filename, String dir)
	{		
		try
		{
			File output = new File(zip_filename);				 
		
			InputStream is = new FileInputStream(output);
			ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream("zip", is);
						
			ZipArchiveEntry entry = null;
			while((entry = (ZipArchiveEntry)in.getNextEntry()) != null) 
			{
			    File outfile = new File(dir + "/" + entry.getName());
			    
			    //System.out.println(outfile.getCanonicalPath());			    
			    
			    if(entry.isDirectory())
			    {
			    	outfile.mkdirs();
			    }
			    else
			    {
				    outfile.getParentFile().mkdirs();	
				    
				    OutputStream out = new FileOutputStream(outfile);
				    IOUtils.copy(in, out);
				    out.close();
			    }
			}
			in.close();
			is.close();	      
			
			in = null;
			is = null;
			
			return true;
		}
		catch(Exception e)
		{			
			e.printStackTrace();
		}			
		
		return false;
	}
	
	public static boolean writeFile(String filename, String html_content, String charset)
	{
		if(filename.length() > 0)
		{
			try
			{
				FileOutputStream out = new FileOutputStream(filename);
				OutputStreamWriter osw = new OutputStreamWriter(out, charset);
				osw.write(html_content);
				osw.close();
				osw = null;
				
				//out.write(html_content.getBytes());
				//out.close();
				
				return true;
			}
			catch(Exception e)
			{			
				e.printStackTrace();
			}			
		}
		
		return false;
	}
	
	public static String read_file(String file_name)
	{
		String file_content = "";
		File file = new File(file_name);
		if (file.isFile()) 
		{
			try 
			{ 
				byte[] buff = new byte[102400]; 
				FileInputStream infile = null; 

				infile = new FileInputStream(file); 
				int n_read = 0;
				while (n_read != -1) 
				{ 
					n_read = infile.read(buff); 
					if(n_read > 0)
					{
						file_content = file_content + new String(buff, 0, n_read);	
					}
				} 

				infile.close(); 		
				infile = null;
			} 
			catch (Exception e) 
			{ 
				file_content = "";
			} 
		} 
		
		return file_content;
	}
	
	public static int read_file(String file_name, byte[] buffer, int offset, int len)
	{		
		File file = new File(file_name);
		int n_read = -1;
		if (file.isFile()) 
		{
			try 
			{ 				
				FileInputStream infile = new FileInputStream(file); 
				n_read = infile.read(buffer, offset, len); 					
				infile.close(); 	
				infile = null;
			} 
			catch (Exception e) 
			{ 
				n_read = -1;
			} 
		} 
		
		return n_read;
	}

	public static String read_file(String filename, String charset)
	{		
		String html_content = "";
		
		if(filename.length() > 0)
		{
			File file_sign_obj = new File(filename);		
			
			try
			{
				FileInputStream infile = new FileInputStream(file_sign_obj);	
				InputStreamReader isr = null; 
				
				if(charset != null && charset.length() > 0 && !charset.equalsIgnoreCase("auto"))
				{
					isr = new InputStreamReader(infile, charset);
				}
				else
				{
					isr = new InputStreamReader(infile);
				}
				
				char[] buff = new char[1024]; 
				int n_read = 0;
				while (n_read != -1) 
				{ 
					n_read = isr.read(buff, 0, 1024); 
					if(n_read > 0)
					{										
						html_content = html_content + new String(buff, 0, n_read);	
					}
				} 
				infile.close(); 
				infile = null;
			}
			catch(Exception e)
			{	
				e.printStackTrace();
			}			
		}
		
		return html_content;			
	}

	public static void deleteDir(File file) {
		if (file.isDirectory()) {
			File[] array = file.listFiles();
			for (int i = 0; i < array.length; i++) 
			{
				deleteDir(array[i]);
			}
			
			file.delete();			
		} else {			
			file.delete();			
		}
	}
	
	/**
	* This class copies an input file to output file
	*
	* @param String input file to copy from
	* @param String output file
	*/
	public static boolean copy(String input, String output) throws Exception{
		int BUFSIZE = 10240000;
		try{
			FileInputStream fis = new FileInputStream(input);
			FileOutputStream fos = new FileOutputStream(output);
	
			int s;
			byte[] buf = new byte[BUFSIZE];
			while ((s = fis.read(buf)) > -1 ){
				fos.write(buf, 0, s);
			}	
			
			fis.close();
			fos.close();
			
			fis = null;
			fos = null;
		}
		catch (Exception ex) {
			throw new Exception("copy file: "+ex.getMessage()); 
		}
		return true;
	}
	
	public static void copyDir(String src, String dest) throws Exception{
		File src_obj = new File(src);	
		
		if(src_obj.isDirectory())
		{
			File dest_obj = new File(dest);
			dest_obj.mkdirs();
			
			File[] array = src_obj.listFiles();
			for (int i = 0; i < array.length; i++) 
			{
				String dest_new = dest + "/" + array[i].getName();				
				copyDir(array[i].getCanonicalPath(), dest_new);
			}
		}
		else
		{
			copy(src, dest);
		}
	}
	
	public static boolean writeFile(String file_name, String content)
	{		
		FileOutputStream outfile = null;
		try
		{								
			outfile = new FileOutputStream(file_name);	
			outfile.write(content.getBytes()); 
			outfile.close(); 
			outfile = null;
			
			return true;
		}
		catch(IOException e)
		{
			if(outfile != null)
			{
				try
				{
					outfile.close();
					outfile = null;
				}
				catch(IOException ex)
				{
					ex.printStackTrace();
				}
			}
			
			e.printStackTrace();
			return false;
		}			
	}
	
	public static boolean writeFile(String file_name, byte[] buffer)
	{	
		return writeFile(file_name, buffer, 0, buffer.length);
	}
	
	public static boolean writeFile(String file_name, byte[] buffer, int offset, int len)
	{		
		try
		{								
			FileOutputStream outfile = new FileOutputStream(file_name);	
			outfile.write(buffer, offset, len); 
			outfile.close(); 
			outfile = null;
			
			return true;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}	
	}
	
	// 2015-7-2
	public static boolean writeFile(String head, String file_name, byte[] buffer, int offset, int len)
	{		
		try
		{								
			FileOutputStream outfile = new FileOutputStream(file_name);	
			
			outfile.write(head.getBytes()); 			
			outfile.write(buffer, offset, len); 
			
			outfile.close(); 
			outfile = null;
			
			return true;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}	
	}
	
	public static int writeFile(String file_name, InputStream bais)
	{
		int total_read = 0;
		FileOutputStream outfile = null;
		
		try
		{
			outfile = new FileOutputStream(file_name);	
			
			byte[] buff = new byte[2048]; 			
			int n_read = 0;			
			
			while (n_read != -1) 
			{ 
				n_read = bais.read(buff, 0, 2048); 
				if(n_read > 0)
				{					
					outfile.write(buff, 0, n_read); 
					total_read += n_read;
				}				
			} 				
			
			outfile.close(); 		
			outfile = null;
			
			return total_read;			
		}
		catch(IOException e)
		{
			if (LOG.isErrorEnabled()) {
				//String body = new String(session.content, start_pos, end_pos-start_pos);
				LOG.error("file_name:"+file_name+", total_read="+total_read+", ����HTTPЭ������ʧ��. err="+e);	
				e.printStackTrace();
			}
			
			return total_read;
		}	
		finally
		{
			if(outfile != null)
			{
				try
				{
					outfile.close();
				} 
				catch(Exception e)
				{
					LOG.error(e);
					e.printStackTrace();
				}
				outfile = null;
			}			
		}		
	}
	
	public static String writeString(InputStream bais)
	{	
		try
		{	
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			byte[] buff = new byte[2048]; 			
			int n_read = 0;			
			
			while (n_read != -1) 
			{ 
				n_read = bais.read(buff, 0, 2048); 
				if(n_read > 0)
				{					
					baos.write(buff, 0, n_read); 					
				}				
			} 				
			
			baos.close(); 		
			baos = null;
			
			return baos.toString();			
		}
		catch(IOException e)
		{
			if (LOG.isErrorEnabled()) {		
				//String body = new String(session.content, start_pos, end_pos-start_pos);
				LOG.error("��ȡHTTPЭ��body����ʧ��. err="+e);	
				e.printStackTrace();
			} 	
			
			return "";
		}	
	}
	
	public static void appendFile(String fileName, String content) {		
		try {				
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.close();
			writer = null;
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public static void appendFile(String fileName, byte[] buffer) {		
		try {			
			File f = new File(fileName);
			
			RandomAccessFile writer = new RandomAccessFile(fileName, "rw");	
			writer.seek(f.length());
		
			writer.write(buffer);
			writer.close();
			writer = null;
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public static void appendFile(String fileName, InputStream bais) {				
		try {			
			File f = new File(fileName);
			
			RandomAccessFile writer = new RandomAccessFile(fileName, "rw");	
			writer.seek(f.length());
			
			byte[] buff = new byte[2048]; 			
			int n_read = 0;			
			
			while (n_read != -1) 
			{ 
				n_read = bais.read(buff, 0, 2048); 
				if(n_read > 0)
				{		
					writer.write(buff, 0, n_read); 
				}				
			} 				
			
			writer.close();
			writer = null;
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args) {
		/*
		String dir = "C:/test";
		File file = new File(dir);
		deleteDir(file);
		*/
		try
		{
			//writeFile("D:\\tmp\\utf8.txt", "��ã� hello", "utf-8");
		
			/*
			 // Archive
	        final File output = new File("D:/tmp/test.zip");
	        final File file1 = new File("D:/tmp/httpd.conf");
	        final File file2 = new File("D:/tmp/httpd-vhosts.conf");

	 
	            final OutputStream out = new FileOutputStream(output);
	            final ArchiveOutputStream os = new ArchiveStreamFactory().createArchiveOutputStream("zip", out);

	            os.putArchiveEntry(new ZipArchiveEntry("testdata/test1.conf"));
	            IOUtils.copy(new FileInputStream(file1), os);
	            os.closeArchiveEntry();

	            os.putArchiveEntry(new ZipArchiveEntry("testdata/test2.conf"));
	            IOUtils.copy(new FileInputStream(file2), os);
	            os.closeArchiveEntry();
	            os.close();
		 	*/

	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		FileOpr.compress_dir("d:/tmp/test.zip", "D:\\tmp\\zip\\");		
		FileOpr.uncompress_dir("d:/tmp/test.zip", "D:\\tmp\\zip_out");		
	}
}
