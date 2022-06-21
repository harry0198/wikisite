import {isFileFormatImage, isFileSizeSensible} from "../modules/validation.mjs";
import {profileImageUpload} from "../modules/util.mjs";
enableDragNDropFunctionality()
import {enableCarouselKeyScroll} from "../modules/carousel-key-scroll.mjs";
import {init as common, onLoad} from "./common.mjs";
import {enableDragNDropFunctionality} from "../modules/dragndrop.mjs";
import Toast from "../modules/toast.mjs";

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
    let el = document.getElementById('imageUpload');
    el.addEventListener('input', function (e) {
        console.log(el.files[0])
        console.log(el.files[0].title)
        console.log(el.files[0].name)
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
    // for (let el of document.getElementsByClassName('description-input')) {
    //     el.addEventListener('input', function () {
    //         for (let title of document.getElementsByClassName('post-description-content')) {
    //             title.textContent = this.value;
    //         }
    //     });
    // }