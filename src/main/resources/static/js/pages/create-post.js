import {isFileFormatImage, isFileSizeSensible} from "../modules/validation.mjs";
import {profileImageUpload} from "../modules/util.mjs";
enableDragNDropFunctionality()
import {enableCarouselKeyScroll} from "../modules/carousel-key-scroll.mjs";
import {init as common, onLoad} from "./common.mjs";
import {enableDragNDropFunctionality} from "../modules/dragndrop.mjs";

onLoad(() => {
    common()
    enableCarouselKeyScroll()
    enableDragNDropFunctionality()
    for (let el of document.getElementsByClassName('title-input')) {
        el.addEventListener('input', function () {
            for (let title of document.getElementsByClassName('card__title')) {
                title.textContent = this.value;
            }
        });
    }
    for (let el of document.getElementsByClassName('image-input')) {
        el.addEventListener('input', function () {
            if (!isFileFormatImage(profileImageUpload.value)) {
                return;
            }
            if (!isFileSizeSensible(profileImageUpload.files[0], 4)) {
                return;
            }
            for (let image of document.getElementsByClassName('card__image')) {
                image.setAttribute('src',
                    window.URL.createObjectURL(profileImageUpload.files[0]));
            }
        });
    }
    for (let el of document.getElementsByClassName('description-input')) {
        el.addEventListener('input', function () {
            for (let title of document.getElementsByClassName('post-description-content')) {
                title.textContent = this.value;
            }
        });
    }
})