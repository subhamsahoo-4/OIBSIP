async function requireAdmin(){
    try{
        const m=await API.me();
        if(m.role!=='ADMIN')throw 0;
        document.querySelectorAll('[data-admin-name]').forEach(x=>x.textContent=m.name);
        return m;
    }catch(e){
        location.href='/login.html';
    }
}

function adminNav(){
    return `<aside class="sidebar">
        <a href="/admin/dashboard.html">Dashboard</a>
        <a href="/admin/manage-books.html">Manage Books</a>
        <a href="/admin/manage-members.html">Members</a>
        <a href="/admin/issued-books.html">Issued Books</a>
        <a href="/admin/fines.html">Fines</a>
        <a href="/admin/inbox.html">Inbox</a>
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

function adminTop(){
    return `<header class="topbar">
        <strong>Digital Library · Admin</strong>
        <div class="topbar-actions">
            ${notificationBellMarkup()}
            <span data-admin-name></span>
            <a href="#" onclick="API.logout(); return false">Logout</a>
        </div>
    </header>`;
}

async function shell(){
    await requireAdmin();
    document.body.insertAdjacentHTML('afterbegin',adminTop());
    document.querySelector('.layout')?.insertAdjacentHTML('afterbegin',adminNav());
    initNotificationBell();
}
