package com.harrydrummond.projecthjd.service;

import com.harrydrummond.projecthjd.posts.Post;
import com.harrydrummond.projecthjd.posts.image.Image;
import com.harrydrummond.projecthjd.posts.image.ImageRepository;

import com.harrydrummond.projecthjd.posts.image.ImageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageServiceImpl imageService;

    private Image image;

    @BeforeEach
    public void generateImage() {

        Post post = new Post();
        post.setDescription("Random desc");
        post.setTitle("Title");
        post.setId(1L);

        image = new Image();
        image.setAlt("Alt");
        image.setPost(post);
        image.setPath("/some/path");
        image.setId(1L);
    }

    @Test
    void getImageById() {
        given(imageRepository.findById(1L)).willReturn(Optional.of(image));

        Image foundImage = imageService.getImageById(1L).get();

        assertThat(foundImage).isNotNull();
    }

//    @Test
//    void saveImage() {
//        given(imageRepository.save(image)).willReturn(image);
//
//        Image savedImage = imageService.saveImage(image);
//
//        assertThat(savedImage).isNotNull();
//    }

    @Test
    void getAllImages() {
        Image image1 = new Image();
        image1.setAlt("Alte");
        image1.setPath("/some/pathe");
        image1.setId(2L);

        given(imageRepository.findAll()).willReturn(List.of(image, image1));

        List<Image> foundImages = imageService.getAllImages();

        assertThat(foundImages).isNotNull();
        assertThat(foundImages.size()).isEqualTo(2);
    }

    @Test
    void updateImage() {
        given(imageRepository.save(image)).willReturn(image);

        image.setPath("diff");
        image.setAlt("diff");
        image.setPost(null);
        image.setId(4L);

        Image updated = imageService.updateImage(image);

        assertThat(updated.getId()).isEqualTo(4L);
        assertThat(updated.getPost()).isNull();
        assertThat(updated.getAlt()).isEqualTo("diff");
        assertThat(updated.getPath()).isEqualTo("diff");
    }

    @Test
    void deleteImage() {
        willDoNothing().given(imageRepository).deleteById(1L);

        imageService.deleteImage(1L);

        verify(imageRepository, times(1)).deleteById(1L);
    }
}