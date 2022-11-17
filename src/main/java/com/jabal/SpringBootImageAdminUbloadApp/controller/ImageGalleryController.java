package com.jabal.SpringBootImageAdminUbloadApp.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.jabal.SpringBootImageAdminUbloadApp.entity.ImageGallery;
import com.jabal.SpringBootImageAdminUbloadApp.entity.User;
import com.jabal.SpringBootImageAdminUbloadApp.service.ImageGalleryService;


@Controller
public class ImageGalleryController {
	
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();

	@Value("${uploadDir}")
	private String uploadFolder;

	@Autowired
	private ImageGalleryService imageGalleryService;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping(value = {"","/"})
	public String redirect(@CurrentSecurityContext(expression="authentication?.name") String username , Model map ) {
		
//		String Authorities =auth.getAuthorities().toString();	
		if(username.equals("admin")) {	
			System.out.println(username + " AdminPage");
			List<ImageGallery> images = imageGalleryService.getAllNonActiveImages();
			map.addAttribute("images", images);
			return "admin";     
		}else {
			List<ImageGallery> images = imageGalleryService.getAllActiveImages();
			map.addAttribute("images", images);	
			return "images"; 
		}		
	}
	
    @GetMapping({"**","/home","/image/show"})
    public String viewHomePage(Model map) {
		List<ImageGallery> images = imageGalleryService.getAllActiveImages();
		map.addAttribute("images", images);	
		return "images"; 
		}
    

	@GetMapping({"/upload"})
	public String showUploadForm(Model model) {
		model.addAttribute("imageGallery",new ImageGallery());
		return "upload";
	}
	
	
	@PostMapping("/image/saveImageDetails")
	public  String createImage(	@CurrentSecurityContext(expression="authentication?.name") String username,
								@RequestParam("image_category") String category, 
								@RequestParam("image_description") String description,final 
								@RequestParam("image") MultipartFile file,
								Model model,
								HttpServletRequest request,
								HttpServletResponse response) {
		try {
			String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
			log.info("uploadDirectory:: " + uploadDirectory);
			String fileName = file.getOriginalFilename();
			String filePath = Paths.get(uploadDirectory, fileName).toString();
			log.info("FileName: " + file.getOriginalFilename());
			if (fileName == null || fileName.contains("..")) {
		        return "redirect:/upload?invalid";
			}
			
			String[] categories = category.split(",");
			String[] descriptions = description.split(",");
			Date createDate = new Date();
			log.info("imageName: " + categories[0]+" "+filePath);
			log.info("imageDescription: " + descriptions[0]);
			try {
				File dir = new File(uploadDirectory);
				if (!dir.exists()) {
					log.info("Folder Created");
					dir.mkdirs();
				}
				// Save the file locally
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
				stream.write(file.getBytes());
				stream.close();
			} catch (Exception e) {
				log.info("in catch");
				e.printStackTrace();
			}
			byte[] imageData = file.getBytes();
			ImageGallery imageGallery = new ImageGallery();
			if(username.equals("admin")) {	
			imageGallery.setEnabled(true);}else {
			imageGallery.setEnabled(false);}	
			imageGallery.setImage_category(categories[0]);
			imageGallery.setImage(imageData);
			imageGallery.setImage_description(descriptions[0]);
			imageGallery.setCreateDate(createDate);
			imageGallery.setImage_user(username);
			imageGalleryService.saveImage(imageGallery);
			log.info("HttpStatus===" + new ResponseEntity<>(HttpStatus.OK));
			if(username.equals("admin")) {	
		        return "redirect:/upload?adminsuccess";}else {
			    return "redirect:/upload?success";}		
		} catch (Exception e) {
			
		    if (e instanceof MaxUploadSizeExceededException) {
		        return "redirect:/upload?limit";
		    }
		    else {
			e.printStackTrace();
			log.info("Exception: " + e);
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	        return "redirect:/upload?bad";
	        }}}
	
	
	
	@GetMapping("/image/display/{id}")
	@ResponseBody
	void showImage(@PathVariable("id") Long id, HttpServletResponse response, Optional<ImageGallery> imageGallery)
			throws ServletException, IOException {
		log.info("Id :: " + id);
		imageGallery = imageGalleryService.getImageById(id);
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		response.getOutputStream().write(imageGallery.get().getImage());
		response.getOutputStream().close();
	}
	

	@GetMapping("/image/imageDetails")
	String showProductDetails(@RequestParam("id") Long id, Optional<ImageGallery> imageGallery, Model model) {
		try {
			log.info("Id :: " + id);
			if (id != 0) {
				imageGallery = imageGalleryService.getImageById(id);
			
				log.info("Image :: " + imageGallery);
				if (imageGallery.isPresent()) {
					model.addAttribute("id", imageGallery.get().getImage_id());
					model.addAttribute("description", imageGallery.get().getImage_description());
					model.addAttribute("createDate", imageGallery.get().getCreateDate());
					model.addAttribute("category", imageGallery.get().getImage_category());
					model.addAttribute("isEnabled", imageGallery.get().isEnabled());
					model.addAttribute("user", imageGallery.get().getImage_user());
					return "imagedetails";
				}
				return "redirect:/home";
			}
			
		return "redirect:/home";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/home";
		}	
	}


    @GetMapping({"/admin"})
    public String viewAdminPage(Model map) {
		List<ImageGallery> images = imageGalleryService.getAllNonActiveImages();
		map.addAttribute("images", images);
		return "admin";    
		}
    
	@GetMapping("/image/imageAccept")
	public String acceptImage(@RequestParam("id") Long id) {
		log.info("Id :: " + id);
		imageGalleryService.acceptImageById(id);
		return "redirect:/admin";

	}
	
	@GetMapping("/image/imageDelete")
	public String deleteImage(@RequestParam("id") Long id) {
		log.info("Id :: " + id);
		imageGalleryService.deleteImageById(id);
		return "redirect:/Home";
	}
	
	@GetMapping("/image/imageReject")
	public String rejectImage(@RequestParam("id") Long id) {
		log.info("Id :: " + id);
		imageGalleryService.deleteImageById(id);
		return "redirect:/admin";

	}
	
    
}	

