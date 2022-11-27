import {isFileFormatImage, isFileSizeSensible} from "../modules/validation.mjs";
import {BASE_URL} from "../modules/util.mjs";
import {enableCarouselKeyScroll} from "../modules/carousel-key-scroll.mjs";
import {init as common, onLoad} from "./common.mjs";
import {enableDragNDropFunctionality} from "../modules/dragndrop.mjs";
import Toast from "../modules/toast.mjs";
import {submitFormRequest, toJsonObject} from "../modules/ajax.mjs";
import {validationFailed, validationPassed} from "../modules/form-validation.mjs";
import * as S from "../lib/Sortable.mjs";

var imgArray = [];
var orderedImages = [];
const IMAGE_ID = 'data-image-id';
onLoad(() => {
    common()
    enableCarouselKeyScroll()
    enableDragNDropFunctionality()

    formSubmission()
    for (let el of document.getElementsByClassName('title-input')) {
        el.addEventListener('input', function () {
            for (let title of document.getElementsByClassName('card__title')) {
                title.textContent = this.value;
            }
        });
    }
    let el = document.getElementById('imageUpload');
    let uploadPreview = document.getElementById('upload-preview')
    new Sortable(uploadPreview, {
        swapThreshold: 1,
        animation: 150,
        onEnd: function (/**Event*/evt) {
            displayChanges()
        }
    });
    el.addEventListener('input', function (e) {

        var files = e.target.files;
        var filesArr = Array.prototype.slice.call(files);
        var iterator = 0;
        filesArr.forEach(function (f, index) {
            let size = f.size / 1024 / 1024;

            if (containsVideo()) {
                Toast("You may only upload one video!", "fa-circle-exclamation")
                return
            }

            if (f.type.match('image.*')) {
                if (size > 10) {
                    Toast("Max file size per image is 10MB!", "fa-circle-exclamation")
                    return
                }
            } else if (f.type.match('video.*')) {
                if (size > 20) {
                    Toast("Max file size per video is 20MB!", "fa-circle-exclamation")
                    return
                }
            } else {
                Toast("Unsupported file format!", "fa-circle-exclamation")
                return
            }

            const maxLength = 3;
            var len = 0;
            for (var i = 0; i < imgArray.length; i++) {
                if (imgArray[i] !== undefined) {
                    len++;
                }
            }
            if (len > maxLength) {
                Toast("You have reached the max amount of uploaded files", "fa-circle-exclamation")
                return false;
            } else {
                imgArray.push(f);

                var reader = new FileReader();
                reader.onload = function (e) {
                    let imgWrapper = document.createElement('div');
                    imgWrapper.classList.add('wrap');
                    imgWrapper.addEventListener('mouseup', () => {
                        displayChanges();
                    })

                    let image = document.createElement('img');
                    let imageURL = window.URL.createObjectURL(f);
                    image.classList.add("card__image", "upload");
                    image.setAttribute('src',
                       imageURL);
                    image.setAttribute('data-image-id', f.name);

                    let closeBtn = document.createElement("button")
                    closeBtn.classList.add('delete-btn');
                    closeBtn.onclick = () => {
                        var file = image.getAttribute(IMAGE_ID);
                        for (var i = 0; i < imgArray.length; i++) {
                            if (imgArray[i].name === file) {
                                imgArray.splice(i, 1);
                                displayChanges()
                                break;
                            }
                        }
                        imgWrapper.remove()
                    }

                    let faCross = document.createElement('i');
                    faCross.classList.add('fa-solid','fa-xmark')

                    closeBtn.appendChild(faCross);
                    imgWrapper.appendChild(image);
                    imgWrapper.appendChild(closeBtn)
                    uploadPreview.appendChild(imgWrapper);

                    displayChanges()

                    iterator++;
                }
                reader.readAsDataURL(f);
            }


        });

        // for (let image of document.getElementsByClassName('card__image')) {
        //     image.setAttribute('src',
        //         window.URL.createObjectURL(el.files[0]));
        // }
    });


})

function containsVideo() {
    imgArray.forEach(x => {
        if (x.type.match('video.*')) {
            return true;
        }
    })
    return false;
}

function displayChanges() {
    orderImages()
    if (orderedImages.length >= 1) {
        updateCardImage(window.URL.createObjectURL(orderedImages[0]));
    } else {
        updateCardImage('');
    }
}

function updateCardImage(activeURL) {
    let it = document.getElementById('thumbnail-card');
    // orderedImages.forEach()
    for (let image of it.getElementsByClassName('card__image')) {
        image.setAttribute('src', activeURL);
    }
}

function orderImages() {
    let uploadPreview = document.getElementById('upload-preview')
    orderedImages = [];
    for (let child of uploadPreview.getElementsByTagName('img')) {
        let id = child.getAttribute(IMAGE_ID)

        imgArray.forEach((f, index) => {
            if (f.name === id) {
                orderedImages.push(f);
            }
        })
    }
}

function formSubmission() {
    // let form = document.getElementById('form-carousel');
    let submitBtn = document.getElementById('submit-post');

    submitBtn.onclick = ((e) => {


        orderImages()

        submitBtn.classList.add('button--loading');
        e.preventDefault();

        const cb = function (s) {
            let status = s.status;
            if (status === 200) {
                Toast("Successfully made post", "fa-circle-check");
                window.location.href = BASE_URL + '/post/view/' + s.responseText;
            } else if (status === 401) {
                Toast("You are not authorized to do this", "fa-circle-exclamation");
            } else if (status === 400) {
                let obj = toJsonObject(s.responseText);
                let title = obj.title;
                let titleField = document.getElementById('title');
                let image = obj.image;
                let imageField = document.getElementById('imageUpload');
                let description = obj.description;
                let descriptionField = document.getElementById('description');
                if (title != null) {
                    validationFailed(titleField, title);
                } else {
                    validationPassed(titleField)
                }
                if (image != null) {
                    validationFailed(imageField, image);
                } else {
                    validationPassed(imageField)
                }
                if (description != null) {

                    validationFailed(descriptionField, description);
                } else {
                    validationPassed(descriptionField)
                }
                Toast("There are errors in your post. Please go back and fix them.", "fa-circle-exclamation");
            } else if (status === 413) {
                Toast("File was too large!", "fa-circle-exclamation");
            } else {
                Toast("Unexpected error: " + status + ". Try again later", "fa-circle-exclamation");
            }
            submitBtn.classList.remove('button--loading');

        }

        let form = document.getElementById('form-carousel');
        let formData = new FormData(form);
        console.log(orderedImages)
        formData.delete('images')
        orderedImages.forEach(file => {
            formData.append('images', file);
        })
        console.log(formData)
        console.log(form)
        submitFormRequest("/api/post/new", cb, formData, 'POST');
    });
}