/**
 * 
 */
package com.app.checkmoney.helper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

import com.app.checkmoney.config.exception.ApiCheckMoneyException;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * @author Linygn Escudero
 *
 */
public class Thumbnailator {
	
	public String upLoadFiles(String rootPath, String folder, MultipartFile file, String namefile, Boolean Thumb, String ThumbPosition) throws ApiCheckMoneyException {
        String ruta;
        String SAVE_DIR = folder;
        String pathSeparador = "";//File.separator;
        String pathFtp = rootPath;
        String savePath = pathFtp + SAVE_DIR + pathSeparador;

        try {
            MultipartFile filePart = file;
            String fileName = filePart.getOriginalFilename();//getSubmittedFileName();
            String fileType = getFileExtension(fileName);
            InputStream filecontent = filePart.getInputStream();
            String NameFileEnd = namefile + "." + fileType;
            String PathFinal = savePath + NameFileEnd;
            Path rutaDestino = Paths.get(PathFinal);

            File directory = new File(pathFtp + folder);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            Files.copy(filecontent, rutaDestino, StandardCopyOption.REPLACE_EXISTING);
            ruta = SAVE_DIR + pathSeparador + NameFileEnd;

            if(Thumb){
                if(ThumbPosition.equals("H")){
                    BufferedImage originalImage = ImageIO.read(new File(PathFinal));
                    double scale = 550/(double)originalImage.getWidth();
                    BufferedImage thumbnail = Thumbnails.of(originalImage)
                            .size(550, 360)
                            .crop(Positions.CENTER)
                            .asBufferedImage();
                    ImageIO.write(thumbnail, fileType, new File(pathFtp + SAVE_DIR + pathSeparador + namefile + "_thumbnail" + "." + fileType));
                }else{
                    BufferedImage originalImage = ImageIO.read(new File(PathFinal));
                    BufferedImage thumbnail = Thumbnails.of(originalImage)
                            .size(360, 550)
                            .crop(Positions.CENTER)
                            .asBufferedImage();
                    ImageIO.write(thumbnail, fileType, new File(pathFtp + SAVE_DIR + pathSeparador + namefile + "_thumbnail" + "." + fileType));
                }
            }
        } catch(IOException excepcion){
            System.out.println(excepcion.getMessage());
            ruta = "ERROR :: " + excepcion.getMessage();
        }
        return ruta;
    }

    public static String getFileExtension(String fullName) {
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

}
