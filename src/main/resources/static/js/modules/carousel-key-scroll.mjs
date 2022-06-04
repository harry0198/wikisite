function enableCarouselKeyScroll() {
    let enterNextPostCreate = document.getElementsByClassName('enter-next');
    for (let el of enterNextPostCreate) {
        el.addEventListener("keypress", function(event) {
            if (event.key === "Enter") {
                document.getElementById('post-flow-next').click();
            }
        });
    }
}
export {
    enableCarouselKeyScroll
}