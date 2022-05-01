

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

let cards = document.getElementsByClassName('article__card');
if (cards != null) {
    for (let card of cards) {
        let cardLink = card.getElementsByClassName('article__card-title');
        let link;
        for (let cardLinkElement of cardLink) {
            link = cardLinkElement.querySelector('a').href;
        }
        let cardHeader = card.getElementsByClassName('article__card-header');
        let cardOverview = card.getElementsByClassName('article__card-overview');
        for (let cardHeaderElement of cardHeader) {
            cardHeaderElement.onclick = () => {
                window.location.href = link;
            }
        }
        for (let cardInfoElement of cardOverview) {
            cardInfoElement.onclick = () => {
                window.location.href = link;
            }
        }
    }
}


let left_mover_btn = document.getElementById('article__popular-move-left');
let right_mover_btn = document.getElementById('article__popular-move-right');
let cardTmp = document.getElementById('article__popular-cards');
let carouselPosts;
if (cardTmp != null) {
    carouselPosts = cardTmp.getElementsByClassName('article__card');
}
if (left_mover_btn != null && right_mover_btn != null && carouselPosts != null) {
    registerCarouselMovers(carouselPosts, left_mover_btn, right_mover_btn);
}

let left_mover_btn_rec = document.getElementById('article__recommended-move-left');
let right_mover_btn_rec = document.getElementById('article__recommended-move-right');
let pstTmp = document.getElementById('article__recommended-cards')
let recommendedPosts;
if (pstTmp != null) {
    recommendedPosts = pstTmp.getElementsByClassName('article__card');
}
if (left_mover_btn_rec != null && right_mover_btn_rec != null && recommendedPosts != null) {
    registerCarouselMovers(recommendedPosts, left_mover_btn_rec, right_mover_btn_rec);
}

function registerCarouselMovers(carouselPosts, left_mover_btn, right_mover_btn) {
    let carousel_pages = Math.ceil(carouselPosts.length / 4);
    let l = 0;
    let movePer = 25.34;
    let maxMove = 203;
// mobile_view
    let mob_view = window.matchMedia("(max-width: 768px)");
    if (mob_view.matches) {
        movePer = 50.36;
        maxMove = 504;
    }

    let right_mover = () => {
        l = l + movePer;
        if (carouselPosts.length === 1) {
            l = 0;
        }
        for (const i of carouselPosts) {
            if (l >= maxMove) {
                l = l - movePer;
            } else {
                i.style.left = '-' + l + '%';
            }
        }

    }

    let left_mover = () => {
        l = l - movePer;
        if (l <= 0) {
            l = 0;
        }
        for (const i of carouselPosts) {
            if (carousel_pages > 1) {
                i.style.left = '-' + l + '%';
            }
        }
    }

    right_mover_btn.onclick = () => {
        right_mover();
    }
    left_mover_btn.onclick = () => {
        left_mover();
    }
}

let profileImagePreview = document.getElementById('profileImageUpload');
let profileImageUpload = document.getElementById('imageUpload');

if (profileImageUpload != null && profileImagePreview != null) {
    profileImageUpload.onchange = () => {
        if (profileImageUpload.files && profileImageUpload.files[0]) {
            profileImagePreview.setAttribute('src',
                window.URL.createObjectURL(profileImageUpload.files[0]));
        }
    }
}

// File#: _2_markdown-editor
// Usage: codyhouse.co/license
(function() {
    function MdEditor(element, actions) {
        this.element = element;
        this.textarea = this.element.getElementsByClassName('js-md-editor__content');
        this.actionsWrapper = this.element.getElementsByClassName('js-md-editor__actions');
        this.actions = actions ? actions : MdEditor.defaults;
        initMdEditor(this);
    };

    function initMdEditor(element) {
        if(element.textarea.length < 1 || element.actionsWrapper.length < 1) return;

        element.actionsWrapper[0].addEventListener('click', function(event){
            insertMdCode(element, event.target.closest('[data-md-action]'));
        });
    };

    function insertMdCode(element, btn) {
        if(!btn) return;
        updateTextareaSelection(element);
        var code = getCode(element, btn.getAttribute('data-md-action'));
        replaceSelectedText(element, code);
    };

    function updateTextareaSelection(element) {
        // store textarea info (e.g., selection range, start and end selection range)
        element.selectionStart = element.textarea[0].selectionStart,
            element.selectionEnd = element.textarea[0].selectionEnd;
        element.selectionContent = element.textarea[0].value.slice(element.selectionStart, element.selectionEnd);
    };

    function getCode(element, action) { // get content to insert in the textarea
        var actionInfo = getActionInfo(element, action)[0]; // returns {action.content, action.newLine}
        if(actionInfo.content == '') return element.selectionContent;
        if(actionInfo.content.indexOf('content') < 0) {
            // e.g. for the lists, we do not modify the selected code but we add an example of how the list is formatted
            element.selectionStart = element.selectionStart + element.selectionContent.length;
            element.selectionContent = '';
        }
        var newContent = actionInfo.content.replace('content', element.selectionContent);
        if(addNewLine(element, actionInfo.newLine)) newContent = '\n'+newContent;
        return newContent;
    };

    function replaceSelectedText(element, text) {
        var value = element.textarea[0].value;
        element.textarea[0].value = value.slice(0, element.selectionStart) + text + value.slice(element.selectionEnd);
        // move focus back to texarea and select text that was previously selected (if any)
        element.textarea[0].focus();
        element.textarea[0].selectionEnd = element.selectionEnd - element.selectionContent.length + text.length - element.actionEndLength;
        if(element.selectionStart != element.selectionEnd) {
            element.textarea[0].selectionStart = element.textarea[0].selectionEnd - element.selectionContent.length;
        } else {
            element.textarea[0].selectionStart = element.textarea[0].selectionEnd;
        }
    };

    function getActionInfo(element, action) {
        var actionInfo = [];
        for(var i = 0; i < Object.keys(element.actions).length; i++) {
            if(element.actions[i].action == action) {
                actionInfo.push(element.actions[i]);
                element.actionEndLength = getEndLength(element.actions[i].content);
                break;
            }
        }
        return actionInfo;
    };

    function addNewLine(element, newLine) {
        if(!newLine) return false;
        if(element.selectionStart < 1) return false;
        if(element.selectionStart > 0) {
            // take character before selectionStart and check if it is a new line
            var previousChar = element.textarea[0].value.slice(element.selectionStart - 1, element.selectionStart);
            return (previousChar.match(/\n/g)) ? false : true;
        }
        return true;
    };

    function getEndLength(string) {
        // e.g. if **content** returns 2, if //content/ returns 1, if ###content returns 0
        var array = string.split('content');
        if(array.length < 2) return 0;
        return array[1].length;
    };

    MdEditor.defaults = [
        {
            action: 'heading',
            content: '##content',
            newLine: false
        },
        {
            action: 'code',
            content: '`content`',
            newLine: false
        },
        {
            action: 'link',
            content: '[content](url)',
            newLine: false
        },
        {
            action: 'image',
            content: '![content](url)',
            newLine: false
        },
        {
            action: 'blockquote',
            content: '> content',
            newLine: true
        },
        {
            action: 'bold',
            content: '**content**',
            newLine: false
        },
        {
            action: 'italic',
            content: '_content_',
            newLine: false
        },
        {
            action: 'uList',
            content: '- Item 1\n- Item 2\n- Item 3',
            newLine: true
        },
        {
            action: 'oList',
            content: '1. Item 1\n2. Item 2\n3. Item 3',
            newLine: true
        },
        {
            action: 'tList',
            content: '- [ ] Item 1\n- [x] Item 2\n- [ ] Item 3',
            newLine: true
        }
    ];

    window.MdEditor = MdEditor;
}());

var mdEditor = document.getElementsByClassName('md-editor')[0]; // your markdown editor element
new MdEditor(mdEditor);