import {isFileFormatImage, isFileSizeSensible} from "../modules/validation.mjs";
import {BASE_URL} from "../modules/util.mjs";
import {enableCarouselKeyScroll} from "../modules/carousel-key-scroll.mjs";
import {init as common, onLoad} from "./common.mjs";
import {enableDragNDropFunctionality} from "../modules/dragndrop.mjs";
import Toast from "../modules/toast.mjs";
import {submitFormRequest, toJsonObject} from "../modules/ajax.mjs";
import {validationFailed, validationPassed} from "../modules/form-validation.mjs";

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
    el.addEventListener('input', function (e) {
        if (!isFileFormatImage(el.files[0].name)) {
            Toast("Invalid file format (.jpg, .jpeg, .png, .gif)", "fa-circle-exclamation")
            el.value = '';
            return;
        }
        if (!isFileSizeSensible(el.files[0], 10)) {
            Toast("Max file size reached! (10mb)", "fa-circle-exclamation")
            el.value = '';
            return;
        }
        for (let image of document.getElementsByClassName('card__image')) {
            image.setAttribute('src',
                window.URL.createObjectURL(el.files[0]));
        }
    });
})

function formSubmission() {
    let form = document.getElementById('form-carousel');
    let submitBtn = document.getElementById('submit-post');

    submitBtn.onclick = ((e) => {
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
                Toast("There were errors in your request", "fa-circle-exclamation");
            } else if (status === 413) {
                Toast("File was too large!", "fa-circle-exclamation");
            } else {
                Toast("Unexpected error: " + status + ". Try again later", "fa-circle-exclamation");
            }
            submitBtn.classList.remove('button--loading');

        }

        let formData = new FormData(form);
        submitFormRequest("/api/post/update", cb, formData, 'POST');
    });
}