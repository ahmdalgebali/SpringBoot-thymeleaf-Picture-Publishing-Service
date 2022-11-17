package com.jabal.SpringBootImageAdminUbloadApp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jabal.SpringBootImageAdminUbloadApp.entity.ImageGallery;
import com.jabal.SpringBootImageAdminUbloadApp.repository.ImageGalleryRepository;



@Service
public class ImageGalleryService {

	@Autowired
	private ImageGalleryRepository imageGalleryRepository;
	
	
	
	public void saveImage(ImageGallery imageGallery) {
		imageGalleryRepository.save(imageGallery);	
	}
	
	public void deleteImageById(Long id) {		
		imageGalleryRepository.deleteById(id);	
	}
	
	public List<ImageGallery> getAllImages() {
		return imageGalleryRepository.findAll();
	}
	
	public List<ImageGallery> getAllActiveImages() {
		return imageGalleryRepository.findByEnabledTrue();
	}
	
	public List<ImageGallery> getAllNonActiveImages() {
		return imageGalleryRepository.findByEnabledFalse();
	}
	
	public void acceptImageById(Long id) {		
		imageGalleryRepository.acceptImageById(id);	
	}

	public Optional<ImageGallery> getImageById(Long id) {
		return imageGalleryRepository.findById(id);
	}
	
}

