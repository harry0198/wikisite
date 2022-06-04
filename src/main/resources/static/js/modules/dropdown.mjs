
function applyDropdownEventHandlers() {
    let dropdownMenus = document.getElementsByClassName('dropdown__menu');
    for (let dropdownMenu of dropdownMenus) {
        for (let dropdownBtn of dropdownMenu.getElementsByClassName('dropdown__btn')) {
            dropdownBtn.onclick = () => {
                dropdownMenu.classList.toggle('open');
            }
        }
        document.addEventListener("click", (event) => {
            const isClickInside = dropdownMenu.contains(event.target);
            if (dropdownMenu.classList.contains('open') && !isClickInside) {
                dropdownMenu.classList.remove('open');
            }
        });
    }
}

export {
    applyDropdownEventHandlers
}