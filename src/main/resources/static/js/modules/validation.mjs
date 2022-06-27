function isFileFormatImage(name) {
    let ext = name.match(/\.([^\.]+)$/)[1];
    switch (ext) {
        case 'jpg':
        case 'png':
        case 'gif':
        case 'jpeg':
        case 'heic':
            return true;
        default:
            return false;
    }
}
function isFileSizeSensible(file, maxFileSizeMb) {
    if(file.size > (1048576 * maxFileSizeMb)){
        return false;
    }
    return true;
}

export {
    isFileSizeSensible,
    isFileFormatImage
}