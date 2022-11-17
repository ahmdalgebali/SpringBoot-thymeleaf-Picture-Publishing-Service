package com.jabal.SpringBootImageAdminUbloadApp.entity;

import java.util.Date;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "image")
public class ImageGallery {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_id", nullable = false, unique = true)
	private Long image_id;
	
	@Column(name = "image_category", nullable = false)
	private String image_category;
	
	@Column(name = "image_description", nullable = false)
	private String image_description;

	@Lob
    @Column(name = "image", nullable = false, unique = true, length = Integer.MAX_VALUE)
    private byte[] image;
	
	@Column(name = "enabled")
    private boolean enabled= Boolean.FALSE;

    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    private Date createDate;
   
	@Column(name = "image_user")
	private String image_user ;


    
}


