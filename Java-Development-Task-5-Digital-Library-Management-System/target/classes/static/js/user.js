async function requireUser(){
    try{
        const m=await API.me();
        if(m.role!=='USER')throw 0;
        document.querySelectorAll('[data-user-name]').forEach(x=>x.textContent=m.name);
        return m;
    }catch(e){
        location.href='/login.html';
    }
}

function userNav(){
    return `<aside class="sidebar">
        <a href="/user/dashboard.html">Dashboard</a>
        <a href="/user/catalogue.html">Catalogue</a>
        <a href="/user/my-issues.html">My Issues</a>
        <a href="/user/reservations.html">Reservations</a>
        <a href="/user/contact.html">Contact</a>
    </aside>`;
}

function notificationBellMarkup(){
    return `<div class="notification-area">
        <button
            type="button"
            class="notification-bell"
            id="notificationBell"
            aria-label="Notifications"
            title="Notifications"
        >
            <svg viewBox="0 0 24 24" aria-hidden="true">
                <path d="M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9M10 21h4" />
            </svg>
            <span id="notificationBadge" class="notification-badge" hidden>0</span>
        </button>

        <div id="notificationPanel" class="notification-panel">
            <div class="notification-panel-header">
                <strong>Notifications</strong>
                <button type="button" id="markAllNotifications" class="notification-mark-all">
                    Mark all as read
                </button>
            </div>
            <div id="notificationList" class="notification-list"></div>
        </div>
    </div>`;
}

function userTop(){
    return `<header class="topbar">
        <strong>Digital Library</strong>
        <div class="topbar-actions">
            ${notificationBellMarkup()}
            <span data-user-name></span>
            <a href="#" onclick="API.logout(); return false">Logout</a>
        </div>
    </header>`;
}

async function userShell(){
    await requireUser();
    document.body.insertAdjacentHTML('afterbegin',userTop());
    document.querySelector('.layout')?.insertAdjacentHTML('afterbegin',userNav());
    initNotificationBell();
}
