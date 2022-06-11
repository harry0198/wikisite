import {postData, patchData, deleteData} from "./ajax.mjs";
import Toast from "./toast.mjs";

let sidebar = document.getElementById("sidebar");

function sidebarFunctionality() {

    let openBtn = document.getElementById('view-fb-btn');
    openBtn.onclick = (e) => {
        openSidebar();
        e.stopImmediatePropagation();
    }

    sidebar.addEventListener('keyup', e => {
        if (e.code === 'Escape') {
            closeSidebar();
        }
    });

    document.addEventListener("click", act);
}

function act(e) {
    if (sidebar.getAttribute("state") === "open") {
        if (sidebar.contains(e.target)) return;
        closeSidebar();
    }
}


function closeSidebar() {
    sidebar.setAttribute("state", "");
}
function openSidebar() {
    sidebar.setAttribute("state", "open");
}

function enableFeedbackFunctionality() {
sidebarFunctionality();
    let feedbackBtn = document.getElementById("feedback-btn");
    feedbackBtn && feedbackBtn.addEventListener('click', leaveFeedback);
    let editFeedback = document.getElementById("PostEditFeedback");

    let editBtns = document.querySelectorAll('[data-feedback-edit]');
    for (let editBtn of editBtns) {
        editBtn.onclick = () => {
            let target = editBtn.getAttribute('data-feedback-edit');
            let comment = document.querySelector('[data-feedback-edit-target="' + target + '"]');
            if (comment == null) return;

            editFeedback.showModal();
            editFeedback.addEventListener('closing', ({target:dialog}) => {
                if (dialog.returnValue === 'confirm') {
                    const cb = function (status) {
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
                        if (status === 200) {
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
}

function saveComment(comment, id, cb) {
    const postId = document.querySelector('meta[name="_post_id"]').content;
    patchData("/api/post/"+postId+"/comment/"+id, cb, comment);
}

function leaveFeedback() {
    const postId = document.querySelector('meta[name="_post_id"]').content;

    let feedback = document.getElementById('feedback-input');

    if (feedback.value === "" || feedback.value.length < 12) {
        Toast("Feedback must be min 12 characters long", "fa-circle-exclamation");
        return;
    }

    let cb = (status) => {
            if (status === 200) {
                Toast("Successfully posted feedback", "fa-circle-check");
                window.location.reload();
            } else {
                Toast("Error: "+status+" Failed to post feedback", "fa-circle-exclamation");
            }
    }

    postData("/api/post/"+postId+"/comment", cb, feedback.value);
}

export {
    enableFeedbackFunctionality
}