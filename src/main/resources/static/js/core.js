let btn = document.querySelectorAll('.like-btn');
for (let btnElement of btn) {
    btnElement.onclick = () => {
        btnElement.classList.toggle('checked');
    }
}

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
    let cardImage = card.getElementsByClassName('card__image');
    for (let cardTitle of cardTitles) {
        let aTag = cardTitle.getElementsByTagName('a');
        for (let aTagElement of aTag) {
            let link = aTagElement.getAttribute("href");
            if (link != null) {
                cardImage.onclick = () => {
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

gsap.registerPlugin(MorphSVGPlugin)

document.querySelectorAll('.bookmark').forEach(button => {
    button.addEventListener('pointerdown', e => {
        if(!button.classList.contains('marked')) {
            return
        }
        gsap.to(button.querySelectorAll('.default, .filled'), {
            morphSVG: 'M26 6H10V18C10 22.6863 11 28 11 28C11 28 17.5273 19.5 18 19.5C18.4727 19.5 25 28 25 28C25 28 26 22.6863 26 18V6Z',
            duration: .15
        })
    })
    button.addEventListener('click', e => {
        e.preventDefault()

        if(button.classList.contains('animated')) {
            return
        }
        button.classList.add('animated')

        if(button.classList.contains('marked')) {
            button.classList.remove('marked')
            gsap.fromTo(button.querySelectorAll('.default, .filled'), {
                morphSVG: 'M26 6H10V18C10 22.6863 11 28 11 28C11 28 17.5273 19.5 18 19.5C18.4727 19.5 25 28 25 28C25 28 26 22.6863 26 18V6Z',
                duration: .15
            }, {
                keyframes: [ {
                    morphSVG: 'M26 6H10V18C10 22.6863 8 31 8 31C8 31 15.9746 26.5 18 23.5C20.0254 26.5 28 31 28 31C28 31 26 22.6863 26 18V6Z',
                    duration: .15
                }, {
                    morphSVG: 'M26 6H10V18V30C10 30 17.9746 23.5 18 23.5C18.0254 23.5 26 30 26 30V18V6Z',
                    duration: .6,
                    ease: 'elastic.out(1, .7)',
                    onComplete() {
                        button.classList.remove('animated')
                    }
                }]
            })
            gsap.to(button, {
                '--default-position': '24px',
                duration: .2,
                clearProps: true
            })
            return
        }

        gsap.to(button, {
            '--icon-background-height': '0px',
            duration: .125,
            delay: .3
        })
        gsap.to(button, {
            '--default-y': '-28px',
            duration: .4
        })
        gsap.to(button.querySelector('.corner'), {
            keyframes: [{
                morphSVG: 'M10 6C10 6 14.8758 6 18 6C21.1242 6 26 6 26 6C26 6 28 8.5 28 10H8C8 8.5 10 6 10 6Z',
                ease: 'none',
                duration: .125
            }, {
                morphSVG: 'M9.99999 6C9.99999 6 14.8758 6 18 6C21.1242 6 26 6 26 6C26 6 31 10.5 26 14H9.99999C4.99999 10.5 9.99999 6 9.99999 6Z',
                ease: 'none',
                duration: .15
            }, {
                morphSVG: 'M7.99998 16.5C7.99998 16.5 9.87579 22.5 18 22.5C26.1242 22.5 28 16.5 28 16.5C28 16.5 31 20 26 23.5H9.99998C4.99998 20 7.99998 16.5 7.99998 16.5Z',
                ease: 'power1.in',
                duration: .125,
                onComplete() {
                    gsap.set(button.querySelector('.corner'), {
                        '--duration': '0s',
                        fill: 'var(--icon-color, var(--icon-color-default))',
                        delay: .05
                    })
                }
            }, {
                morphSVG: 'M8 28C8 28 12.8758 28.5 18 25.5C23.1242 28.5 28 27.5 28 27.5C28 27.5 26 24 26 23.5H10C10 25 8 28 8 28Z',
                ease: 'none',
                duration: .125,
                onComplete() {
                    gsap.set(button.querySelector('.corner'), {
                        '--duration': '.5s',
                        onComplete() {
                            button.classList.add('marked')
                        }
                    })
                }
            }, {
                morphSVG: 'M10 30C10 30 17.8758 23.5 18 23.5C18.1242 23.5 26 30 26 30C26 30 26 23.5 26 23H10C10 24.5 10 30 10 30Z',
                ease: 'elastic.out(1, .7)',
                duration: .5,
                onComplete() {
                    button.classList.remove('animated')
                    gsap.set(button, {
                        '--default-y': '0px',
                        '--default-position': '0px',
                        '--icon-background-height': '19px'
                    })
                    gsap.set(button.querySelector('.corner'), {
                        morphSVG: 'M10 6C10 6 14.8758 6 18 6C21.1242 6 26 6 26 6C26 6 26 6 26 6H10C10 6 10 6 10 6Z',
                        clearProps: true
                    })
                }
            }]
        })
    })
})