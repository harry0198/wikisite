import {BASE_URL} from "./util.mjs";

function enableImageFallbackFunctionality() {
    const imgs = document.querySelectorAll('img');

    imgs.forEach(img => {
        console.log("found img");
        img.addEventListener('error', function handleError() {
            console.log("here")
            const defaultImage =
                `${BASE_URL}/images/local-file-not-found.png`;

            img.src = defaultImage;
            img.alt = 'Fallback image';
        });
    })

}

export {
    enableImageFallbackFunctionality
}