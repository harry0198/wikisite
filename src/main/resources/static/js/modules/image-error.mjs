import {BASE_URL} from "./util.mjs";

function enableImageFallbackFunctionality() {
    const imgs = document.querySelectorAll('img');

    imgs.forEach(img => {
        if (img.src === (BASE_URL + "/")) {
            fixBrokenImage(img);
        }
        img.onerror = () => {
            fixBrokenImage(img)
        }
    })

}

function fixBrokenImage(img) {
    const defaultImage =
        `${BASE_URL}/images/local-file-not-found.png`;

    img.src = defaultImage;
    img.alt = 'Fallback image';
}

export {
    enableImageFallbackFunctionality
}