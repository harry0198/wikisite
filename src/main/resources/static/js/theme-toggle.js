const storageKey = 'theme-preference';

const onClick = () => {
    theme.value = theme.value === 'light' ? 'dark' : 'light';


    setPreference();
}

const getColorPreference = () => {
    if (localStorage.getItem(storageKey))
        return localStorage.getItem(storageKey);
    else
        return window.matchMedia('(prefers-color-scheme: dark)').matches
            ? 'dark'
            : 'light';
}

const setPreference = () => {
    localStorage.setItem(storageKey, theme.value)
    reflectPreference()
}

const reflectPreference = () => {
    document.firstElementChild.setAttribute('page-style', theme.value)
    // update preference btn
    for (let elementsByClassNameElement of document.getElementsByClassName('pagestyle')) {
        elementsByClassNameElement.addEventListener('click', () => {
            document.getElementById('pagestyle')?.setAttribute('aria-label', theme.value);
        });
    }
}

const theme = {
    value: getColorPreference(),
}

// Set early to prevent page flashes
reflectPreference();

window.onload = () => {
    reflectPreference()

    // query selector
    for (let elementsByClassNameElement of document.getElementsByClassName('pagestyle')) {
        elementsByClassNameElement.addEventListener('click', () => {
            onClick();
        });
    }
}

window
.matchMedia('(prefers-color-scheme: dark)')
.addEventListener('change', ({matches:isDark}) => {
    theme.value = isDark ? 'dark' : 'light'
    setPreference()
})