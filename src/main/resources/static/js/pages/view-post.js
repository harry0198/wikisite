import {init as common, onLoad} from "./common.mjs";
import {deleteData, patchData, postData} from "../modules/ajax.mjs";
import Toast from "../modules/toast.mjs";
import {BASE_URL} from "../modules/util.mjs";

onLoad(() => {
    common();
    initSafeLinks()
    handleFeedback()
})

function initSafeLinks() {
    let linkElements = document.getElementsByClassName('user-link');
    let areYouSure = document.getElementById('UnsafeLink');
    for (let linkElement of linkElements) {
        linkElement.onclick = () => {
            let text = linkElement.textContent;
            let linkModal = document.getElementById('UnsafeLink-text');
            linkModal.innerText = text;
            areYouSure.showModal();
            areYouSure.addEventListener('closing', ({target:dialog}) => {
                if (dialog.returnValue === 'confirm') {
                    window.open(text);
                }
            });
        }
    }
}

function handleFeedback() {

    const postId = document.querySelector('meta[name="_post_id"]').content;

    let form = document.getElementById('post-feedback-form');
    form.onsubmit = (e) => {
        console.log(form.getAttribute('data-require-signin'))
        if (form.getAttribute('data-require-signin') === "true") {
            let signUp = document.getElementById('signUpDialog');
            signUp.showModal();
            e.preventDefault()
            return;
        }
        e.preventDefault();
        leaveFeedback()
    }

    let editFeedback = document.getElementById('PostEditFeedback');
    let editBtns = document.querySelectorAll('[data-feedback-edit]');
    for (let editBtn of editBtns) {
        editBtn.onclick = () => {
            let target = editBtn.getAttribute('data-feedback-edit');
            let comment = document.querySelector('[data-feedback-edit-target="' + target + '"]');
            if (comment == null) return;

            editFeedback.showModal();
            editFeedback.addEventListener('closing', ({target:dialog}) => {
                if (dialog.returnValue === 'confirm') {
                    const cb = function (s) {
                        let status = s.status;
                        if (status === 200) {
                            comment.textContent = document.getElementById("feedback-edit-input").value;
                            Toast("Feedback updated", "fa-circle-check");
                        } else if (status === 401) {
                            Toast("You are not authorized to do this", "fa-circle-exclamation");
                        } else {
                            Toast("Error: "+status+" Please try again later.", "fa-circle-exclamation");
                        }
                    }

                    saveComment(document.getElementById("feedback-edit-input").value, target, cb);
                }
            }, {once: true});
        }
    }

    let deleteBtns = document.querySelectorAll('[data-feedback-delete-id]');
    let deleteConfirmation = document.getElementById('DeleteConfirmation');
    for (let deleteBtn of deleteBtns) {
        deleteBtn.onclick = () => {

            deleteConfirmation.showModal();

            deleteConfirmation.addEventListener('closing', ({target:dialog}) => {
                const postId = document.querySelector('meta[name="_post_id"]').content;
                const commentId = deleteBtn.getAttribute('data-feedback-delete-id');
                if (dialog.returnValue === 'confirm') {
                    const cb = function (status) {
                        if (status.status === 200) {
                            Toast("Feedback deleted successfully", "fa-circle-check");
                            location.reload();
                        } else if (status === 401) {
                            Toast("You are not authorized to do this", "fa-circle-exclamation");
                        } else {
                            Toast("Error: "+status+" Please try again later.", "fa-circle-exclamation");
                        }
                    }

                    deleteData("/api/post/"+postId+"/comment/"+commentId, cb, "");
                }
            }, {once: true});
        }
    }

    let deletePostBtn = document.getElementById('delete-post-btn');
    let deletePostConfirm = document.getElementById('DeletePostConfirmation');
    if (deletePostBtn != null) {
        deletePostBtn.onclick = () => {
            deletePostConfirm.showModal();
            deletePostConfirm.addEventListener('closing', ({target: dialog}) => {

                if (dialog.returnValue === 'confirm') {
                    const cb = function (status) {
                        if (status.status === 200) {
                            window.location.href = BASE_URL;
                        } else if (status.status === 401) {
                            Toast("You are not authorized to do this", "fa-circle-exclamation");
                        } else {
                            Toast("Error: " + status.status + " Please try again later.", "fa-circle-exclamation");
                        }
                    }

                    deleteData("/api/post/" + postId, cb, "");
                }
            }, {once: true});
        }
    }

    let shareBtn = document.getElementById('share-btn');
    shareBtn.onclick = () => {
        /* Copy the text inside the text field */
        navigator.clipboard.writeText(window.location.href).then(r => {
            Toast("Copied URL to clipboard", "da-circle-check");
        });
    }

}


function saveComment(comment, id, cb) {
    const postId = document.querySelector('meta[name="_post_id"]').content;
    patchData("/api/post/"+postId+"/comment/"+id, cb, comment);
}

function leaveFeedback() {
    const postId = document.querySelector('meta[name="_post_id"]').content;

    let feedback = document.getElementById('post-feedback-input');

    if (feedback.value === "" || feedback.value.length < 12) {
        Toast("Please give more descriptive feedback", "fa-circle-exclamation");
        return;
    }

    let cb = (status) => {
        if (status.status === 200) {
            Toast("Successfully posted feedback", "fa-circle-check");
            window.location.reload();
        } else if (status.status === 403) {
            Toast("You must be logged in to leave feedback", "fa-circle-exclamation");
        } else if (status.status === 409) {
            Toast("Please edit your existing feedback instead of posting more", "fa-circle-exclamation");
        } else {
            Toast("Unexpected Error: "+status+" Failed to post feedback", "fa-circle-exclamation");
        }
    }

    postData("/api/post/"+postId+"/comment", cb, feedback.value);
}