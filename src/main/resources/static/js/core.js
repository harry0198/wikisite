

// Navbar logic
let menu = document.querySelector('#menu-bars');
let navbar = document.querySelector('.navbar');
let search = document.querySelector('#search');

if (menu != null){
    menu.onclick = () => {
        menu.classList.toggle('fa-times');
        navbar.classList.toggle('active');
    }
}

if (search != null) {
    search.onclick = () => {
        window.location.href = "https://www.vesudatutorials.com/search";
    }
}

let openMenu = document.querySelectorAll('.nav-menu-btn');
let m = document.querySelector('.site-nav');

openMenu.forEach(btn => {
    btn.onclick = () => {
        m.classList.toggle('active');
    }
});

// modal logic



const openModalButtons = document.querySelectorAll("[data-modal-target]");
const closeModalButtons = document.querySelectorAll("[data-close-button]");
const overlay = document.getElementById('overlay');

if (overlay != null && openModalButtons != null && closeModalButtons != null) {
    openModalButtons.forEach(button => {
        button.addEventListener('click', () => {
            const modal = document.querySelector(button.dataset.modalTarget)
            openModal(modal);
        });
    })
    closeModalButtons.forEach(button => {
        button.addEventListener('click', () => {
            const modal = button.closest('._modal');
            closeModal(modal);
        });
    });

    overlay.addEventListener('click', () => {
        const modals = document.querySelectorAll('._modal.active');
        modals.forEach(modal => {
            closeModal(modal);
        })
    })

    function openModal(modal) {
        if (modal == null) {
            return;
        }
        modal.classList.add('active');
        overlay.classList.add('active');
    }

    function closeModal(modal) {
        if (modal == null) {
            return;
        }
        modal.classList.remove('active');
        overlay.classList.remove('active');
    }
}

let profileImagePreview = document.getElementById('profileImageUpload');
let profileImageUpload = document.getElementById('imageUpload');

if (profileImageUpload != null && profileImagePreview != null) {
    profileImageUpload.onchange = () => {
        if (profileImageUpload.files && profileImageUpload.files[0]) {
            if (!checkFileFormatIsImage(profileImageUpload.value)) {
                alert('Invalid file type. Valid formats are .jpg, .png, .svg and .jpeg');
                return;
            }
            if (!checkFileSize(profileImageUpload.files[0].size)) {
                alert("File is too large! Max file size is 4MB");
                return;
            }
            profileImagePreview.setAttribute('src',
                window.URL.createObjectURL(profileImageUpload.files[0]));
        }
    }
}
function checkFileFormatIsImage(name) {
    console.log(name)
    var ext = name.match(/\.([^\.]+)$/)[1];
    switch (ext) {
        case 'jpg':
        case 'png':
        case 'svg':
        case 'jpeg':
            return true;
        default:
            this.value = '';
            return false;
    }
}
function checkFileSize(fileSize) {
    if(fileSize > (2097152 * 2)){
        this.value = "";
        return false;
    }
    return true;
}

let contentEditor = document.getElementById("content-editor");
if (contentEditor != null) {
    let simplemde = new SimpleMDE({ element: contentEditor });
}

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

let cards = document.getElementsByClassName('card__container');
for (let card of cards) {
    let cardTitles = card.getElementsByClassName('card__title');
    for (let cardTitle of cardTitles) {
        let aTag = cardTitle.getElementsByTagName('a');
        for (let aTagElement of aTag) {
            let link = aTagElement.getAttribute("href");
            if (link != null) {
                card.onclick = () => {
                    window.location.href = link;
                }
                break;
            }
        }
    }
}

let dragndrops = document.getElementsByClassName('file-input');
let dragndropcontainers = document.getElementsByClassName('file-container')
for (let dragndrop of dragndrops) {
    dragndrop.addEventListener("dragenter", event => {
        // highlight potential drop target when the draggable element enters it

        addClass2Container()
    });

    dragndrop.addEventListener("dragleave", event => {
        // reset background of potential drop target when the draggable element leaves it
        removeClass2Container()
    });

    dragndrop.addEventListener("mouseout", event => {
        // reset background of potential drop target when the draggable element leaves it
        removeClass2Container()
    });
}

function addClass2Container() {
    for (let dragndropcontainer of dragndropcontainers) {
        dragndropcontainer.classList.add("drag-active");
    }
}
function removeClass2Container() {
    for (let dragndropcontainer of dragndropcontainers) {
        dragndropcontainer.classList.remove("drag-active");
    }
}

let cancelEnterSubmitInput = document.getElementsByClassName('no-submit');
for (let el of cancelEnterSubmitInput) {
    el.addEventListener("keypress", function(event) {
        if (event.key === "Enter") {
            event.preventDefault()
        }
    });
}

let enterNextPostCreate = document.getElementsByClassName('enter-next');
for (let el of enterNextPostCreate) {
    el.addEventListener("keypress", function(event) {
        if (event.key === "Enter") {
            document.getElementById('post-flow-next').click();
        }
    });
}

let form = document.getElementById('form-carousel');
if (form != null) {
    for (let el of document.getElementsByClassName('title-input')) {
        el.addEventListener('input', function (event) {
            for (let title of document.getElementsByClassName('card__title')) {
                title.textContent = this.value;
            }
        });
    }
    for (let el of document.getElementsByClassName('image-input')) {
        el.addEventListener('input', function (event) {
            if (!checkFileFormatIsImage(profileImageUpload.value)) {
                return;
            }
            if (!checkFileSize(profileImageUpload.files[0].size)) {
                return;
            }
            for (let image of document.getElementsByClassName('card__image')) {
                image.setAttribute('src',
                    window.URL.createObjectURL(profileImageUpload.files[0]));
            }
        });
    }
}