package com.jabal.SpringBootImageAdminUbloadApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jabal.SpringBootImageAdminUbloadApp.entity.ImageGallery;


@Repository
public interface ImageGalleryRepository extends JpaRepository<ImageGallery, Long>{
	

	@Query
	public List<ImageGallery> findByEnabledTrue();

	
	@Query
	public List<ImageGallery> findByEnabledFalse();

	@Modifying
	@Query("update ImageGallery u set u.enabled=1 where image_id=?1")
	@Transactional
	public void acceptImageById(Long id);
    
//    @Query("SELECT u FROM image_gallery u WHERE u.image_approved  = false")
//    public List<ImageGallery> getAllNonActiveImages();
}

