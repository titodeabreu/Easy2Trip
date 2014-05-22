package com.easy2trip.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import javax.imageio.*;
import javax.imageio.stream.FileImageOutputStream;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.UploadedFile;

public class SalvaArquivoHelper implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String pastaDeImagems = "resources";
	private static final String imagemExtencao = ".JPG";
	//private static Logger logger = Logger.getLogger(SalvaArquivoHelper.class);

	public File salvarImagem(String nomeArquivo, UploadedFile arquivo, String path) throws IOException {
		
		InputStream input = new BufferedInputStream(arquivo.getInputstream());
		File file = new File(path + File.separator + pastaDeImagems + File.separator + nomeArquivo + imagemExtencao);
		
		System.out.println(file.getName());
		FileOutputStream output = new FileOutputStream(file);	
		
		while (input.available() != 0) {
			output.write(input.read());	
		}
		
		output.close();
		return file;
	}
	public File salvarImagemReduzida(String nomeArquivo, UploadedFile arquivo, String path) throws IOException{
				
		File file = new File(path + File.separator + pastaDeImagems + File.separator  + nomeArquivo + imagemExtencao);
		File file2 = new File(path + File.separator + pastaDeImagems + File.separator  + nomeArquivo + "_p"+ imagemExtencao);
				
		BufferedImage imagem = ImageIO.read(arquivo.getInputstream());
		
		//logger.info("@@ Salvando Arquivo no path: " + file.getAbsolutePath());
		
		BufferedImage novaImagem = new BufferedImage(800, 600, imagem.getType());
		BufferedImage novaImagem2 = new BufferedImage(80, 60, imagem.getType());
		
		Graphics2D g = novaImagem.createGraphics();
		Graphics2D g2 = novaImagem2.createGraphics();
		
	    g.drawImage(imagem, 0, 0, 800, 600, null);
	    g2.drawImage(imagem, 0, 0, 80, 60, null);
		
		ImageIO.write(novaImagem, "JPG", file);
		ImageIO.write(novaImagem2, "JPG", file2);
		
		return file;

	}
	public  String  crop(String path, CroppedImage cropped)  {

		String  newFileName  =  path  +  
				File.separator  + 
				cropped.getOriginalFilename()
				;
		System.out.println(path);
		FileImageOutputStream  imageOutput;
		try  {
			imageOutput  =  new  FileImageOutputStream(new  File(newFileName));
			imageOutput.write(cropped.getBytes(),  0, cropped.getBytes().length);
			imageOutput.close();
		}  catch  (Exception  e)  {
			e.printStackTrace();
		}
		return "";
	}
}

