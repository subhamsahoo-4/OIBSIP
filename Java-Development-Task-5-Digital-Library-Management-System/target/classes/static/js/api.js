const API = {
    async request(url, options = {}) {
        const res = await fetch(url, {
            headers: {
                "Content-Type": "application/json",
                ...(options.headers || {})
            },
            ...options
        });

        if (res.status === 204) {
            return null;
        }

        let data = null;

        try {
            data = await res.json();
        } catch {}

        if (!res.ok) {
            throw new Error(
                data?.message || `Request failed (${res.status})`
            );
        }

        return data;
    },

    get(url) {
        return this.request(url);
    },

    post(url, body) {
        return this.request(url, {
            method: "POST",
            body: JSON.stringify(body)
        });
    },

    put(url, body) {
        return this.request(url, {
            method: "PUT",
            body: JSON.stringify(body)
        });
    },

    patch(url, body) {
        return this.request(url, {
            method: "PATCH",
            body: JSON.stringify(body)
        });
    },

    del(url) {
        return this.request(url, {
            method: "DELETE"
        });
    },

    async me() {
        return this.get("/api/auth/me");
    },

    async logout() {
        await this.post("/api/auth/logout");
        location.href = "/login.html";
    }
};

function esc(v) {
    return String(v ?? "").replace(
        /[&<>'"]/g,
        c => ({
            "&": "&amp;",
            "<": "&lt;",
            ">": "&gt;",
            "'": "&#39;",
            '"': "&quot;"
        }[c])
    );
}

function money(v) {
    return `₹${Number(v).toFixed(2)}`;
}

function date(v) {
    return v
        ? new Date(v).toLocaleDateString("en-IN")
        : "";
}

// =========================================================
// Website notifications
// =========================================================

function showNotification(message, type = "info") {
    let notification = document.getElementById("siteNotification");

    if (!notification) {
        notification = document.createElement("div");
        notification.id = "siteNotification";
        notification.className = "site-notification";
        document.body.appendChild(notification);
    }

    notification.textContent = message;
    notification.className = "site-notification show " + type.toLowerCase();

    clearTimeout(window.notificationTimer);

    window.notificationTimer = setTimeout(() => {
        notification.classList.remove("show");
    }, 2500);
}

window.alert = function(message) {
    showNotification(message, "info");
};

// =========================================================
// Notification bell
// =========================================================

let notificationRefreshTimer = null;

async function loadNotificationBell() {
    const bell = document.getElementById("notificationBell");
    const list = document.getElementById("notificationList");
    const badge = document.getElementById("notificationBadge");

    if (!bell || !list || !badge) {
        return;
    }

    try {
        const [notifications, unread] = await Promise.all([
            API.get("/api/notifications"),
            API.get("/api/notifications/unread-count")
        ]);

        badge.textContent = unread > 99 ? "99+" : unread;
        badge.hidden = unread === 0;

        list.innerHTML = "";

        if (!notifications.length) {
            list.innerHTML = `
                <div class="notification-empty">
                    No notifications yet.
                </div>
            `;
            return;
        }

        notifications.forEach(n => {
            const item = document.createElement("button");
            item.type = "button";
            item.className =
                "notification-item" + (n.read ? "" : " unread");

            item.innerHTML = `
                <span class="notification-item-icon ${String(n.type || "INFO").toLowerCase()}">
                    ${notificationIcon(n.type)}
                </span>
                <span class="notification-item-content">
                    <span class="notification-item-message">${esc(n.message)}</span>
                    <span class="notification-item-time">${notificationDate(n.createdAt)}</span>
                </span>
                ${n.read ? "" : '<span class="notification-dot"></span>'}
            `;

            item.addEventListener("click", async () => {
                if (!n.read) {
                    try {
                        await API.patch(`/api/notifications/${n.id}/read`);
                        await loadNotificationBell();
                    } catch (e) {
                        console.error("Failed to mark notification as read", e);
                    }
                }
            });

            list.appendChild(item);
        });
    } catch (e) {
        console.error("Failed to load notifications", e);
    }
}

function notificationDate(value) {
    if (!value) {
        return "";
    }

    return new Date(value).toLocaleString("en-IN", {
        day: "numeric",
        month: "short",
        year: "numeric",
        hour: "numeric",
        minute: "2-digit"
    });
}

function notificationIcon(type) {
    switch (String(type || "INFO").toUpperCase()) {
        case "FINE":
            return "₹";
        case "ISSUE":
            return "▣";
        case "RETURN":
            return "↩";
        case "RESERVATION":
            return "⌛";
        case "MESSAGE":
            return "✉";
        case "MEMBER":
            return "●";
        case "BOOK":
            return "▤";
        case "ACCOUNT":
            return "✓";
        case "WARNING":
            return "!";
        default:
            return "i";
    }
}

function initNotificationBell() {
    const bell = document.getElementById("notificationBell");
    const panel = document.getElementById("notificationPanel");
    const markAll = document.getElementById("markAllNotifications");

    if (!bell || !panel) {
        return;
    }

    if (bell.dataset.initialized === "true") {
        loadNotificationBell();
        return;
    }

    bell.dataset.initialized = "true";

    bell.addEventListener("click", event => {
        event.stopPropagation();
        panel.classList.toggle("show");

        if (panel.classList.contains("show")) {
            loadNotificationBell();
        }
    });

    panel.addEventListener("click", event => {
        event.stopPropagation();
    });

    if (markAll) {
        markAll.addEventListener("click", async () => {
            try {
                await API.patch("/api/notifications/read-all");
                await loadNotificationBell();
            } catch (e) {
                showNotification(e.message, "error");
            }
        });
    }

    document.addEventListener("click", () => {
        panel.classList.remove("show");
    });

    loadNotificationBell();

    clearInterval(notificationRefreshTimer);
    notificationRefreshTimer = setInterval(
        loadNotificationBell,
        30000
    );
}
