function isFileFormatImage(name) {
    let ext = name.match(/\.([^\.]+)$/)[1];
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
function isFileSizeSensible(file, maxFileSizeMb) {
    if(file.size > (1048576 * maxFileSizeMb)){
        this.value = "";
        return false;
    }
    return true;
}

export {
    isFileSizeSensible,
    isFileFormatImage
}