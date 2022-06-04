

function applyLikeBtnEventHandlers() {
    let btn = document.querySelectorAll('.like-btn');
    for (let btnElement of btn) {
        btnElement.onclick = () => {
            btnElement.classList.toggle('checked');
        }
    }
}

export {
    applyLikeBtnEventHandlers
}