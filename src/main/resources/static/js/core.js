let feedbackBtn = document.getElementById("feedback-btn");
feedbackBtn && feedbackBtn.addEventListener('click', leaveFeedback);

let editBtns = document.querySelectorAll('[data-feedback-edit]');
for (let editBtn of editBtns) {
    editBtn.onclick = () => {
        let target = editBtn.getAttribute('data-feedback-edit');
        let comment = document.querySelector('[data-feedback-edit-target="'+target+'"]');
        if (comment == null) return;

        comment.setAttribute("contenteditable", "true");
        comment.focus();

        var enterKeyFunc = function(event) {
            // If the user presses the "Enter" key on the keyboard
            if (event.key === "Enter") {
                // Cancel the default action, if needed
                event.preventDefault();
                // Trigger the button element with a click
                comment.setAttribute("contenteditable", "false");
                saveComment(comment.textContent, target);
                comment.removeEventListener('keypress', enterKeyFunc);
            }
        }

        var focusLostFunc = function(event) {
            comment.setAttribute("contenteditable", "false");
            saveComment(comment.textContent, target);
            comment.removeEventListener('keypress', focusLostFunc);
        }

        comment.addEventListener("keypress", enterKeyFunc);
        comment.addEventListener("focusout", focusLostFunc);
    }
}

function saveComment(comment, id) {
    var postId = document.querySelector('meta[name="_post_id"]').content;
    let httpRequest = prepareRequest('http://localhost:8080/api/post/' + postId + "/comment/" + id, 'PATCH');

    httpRequest.onreadystatechange = function () {
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                //todo
            }
        }
    }
    httpRequest.send(comment);
}

let deleteBtns = document.querySelectorAll('[data-feedback-delete-id]');
for (let deleteBtn of deleteBtns) {
    deleteBtn.onclick = () => {
        var postId = document.querySelector('meta[name="_post_id"]').content;
        var commentId = deleteBtn.getAttribute('data-feedback-delete-id');
        let httpRequest = prepareRequest('http://localhost:8080/api/post/'+postId+"/comment/"+commentId, 'DELETE');

        let confirmed = confirm("Are you sure you want to delete this comment? This action cannot be undone!");

        if (!confirmed) return;
        console.log('here');

        httpRequest.onreadystatechange = function () {
            if (httpRequest.readyState === XMLHttpRequest.DONE) {
                if (httpRequest.status === 200) {
                    location.reload();
                } else if (httpRequest.status === 401) {
                    alert("You are not authorized to delete this comment!");
                } else if (httpRequest.status === 404) {
                    alert("Could not find comment to delete! Has it already been deleted?");
                } else {
                    alert("Could not delete comment. Status code: " + httpRequest.status);
                }
            }
        }

        httpRequest.send();
    }
}

function leaveFeedback() {
    var postId = document.querySelector('meta[name="_post_id"]').content;
    var httpRequest = prepareRequest("http://localhost:8080/api/post/"+postId+"/comment", 'POST');

    let feedback = document.getElementById('feedback-input');

    // if (feedback.textContent === "" || feedback.textContent.length < 12) {
    //     let feedbackInvalid = document.getElementById('feedback-invalid');
    //     feedbackInvalid.textContent = "Please provide descriptive feedback! Min 12 characters";
    //     feedbackInvalid.classList.remove('hidden');
    //     return;
    // }

    httpRequest.onreadystatechange = () => {
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            let feedbackInvalid = document.getElementById('feedback-invalid');
            if (httpRequest.status === 200) {
                location.reload();
            } else if (httpRequest.status === 404) {
                feedbackInvalid.textContent = "Sorry, we're unable to post your comment! Please try again later.";
                feedbackInvalid.classList.remove('hidden');
            } else {
                feedbackInvalid.textContent = "Unknown Error! Status code: " + httpRequest.status;
                feedbackInvalid.classList.remove('hidden');
            }
        }
    }

    httpRequest.send(feedback.value);
}

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
            console.log(link);
            if (link != null) {
                for (let cardImageElement of cardImage) {
                    cardImageElement.onclick = () => {
                        window.location.href = link;
                    }
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
    for (let el of document.getElementsByClassName('description-input')) {
        el.addEventListener('input', function (event) {
            for (let title of document.getElementsByClassName('post-description-content')) {
                title.textContent = this.value;
            }
        });
    }
}

var feedbackContainers = document.getElementsByClassName('feedback-container');
for (let feedbackContainer of feedbackContainers) {
    let collapsible = feedbackContainer.getElementsByClassName('collapse');
    let btns = feedbackContainer.getElementsByClassName('user__widget_btn');
    for (let collapsibleElement of collapsible) {
        collapsibleElement.addEventListener('show.bs.collapse', function () {
            for (let btn1 of btns) {
                btn1.classList.add('active');
            }
        });
        collapsibleElement.addEventListener('hide.bs.collapse', function () {
            for (let btn1 of btns) {
                btn1.classList.remove('active');
            }
        })
    }

}