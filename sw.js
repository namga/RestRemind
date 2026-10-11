const CACHE_NAME = "nhac-nho-v5";
const ASSETS = [
  "/",
  "/index.html",
  "/manifest.json",
  "/icon-192.png",
  "/icon-512.png",
  "/sw.js"
];

self.addEventListener("install", (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => cache.addAll(ASSETS)).then(() => self.skipWaiting())
  );
});

self.addEventListener("activate", (event) => {
  event.waitUntil(
    caches.keys().then((keys) =>
      Promise.all(keys.filter((k) => k !== CACHE_NAME).map((k) => caches.delete(k)))
    ).then(() => self.clients.claim())
  );
});

self.addEventListener("fetch", (event) => {
  if (event.request.method !== "GET") return;
  event.respondWith(
    caches.match(event.request).then((cached) => {
      if (cached) return cached;
      return fetch(event.request)
        .then((res) => {
          const copy = res.clone();
          if (res.ok && event.request.url.startsWith(self.location.origin)) {
            caches.open(CACHE_NAME).then((cache) => cache.put(event.request, copy));
          }
          return res;
        })
        .catch(() => caches.match("/index.html"));
    })
  );
});

// Nhận lệnh từ trang chính để hiện thông báo (hoạt động tốt hơn khi tab nền)
self.addEventListener("message", (event) => {
  const data = event.data;
  if (!data || data.type !== "SHOW_NOTIFICATION") return;
  event.waitUntil(
    self.registration.showNotification(data.title || "Nhắc nhở", {
      body: data.body || "",
      icon: data.icon || "/icon-192.png",
      badge: "/icon-192.png",
      tag: data.tag || "reminder",
      renotify: true,
      requireInteraction: true,
      vibrate: [200, 100, 200],
      data: { url: "/" }
    })
  );
});

self.addEventListener("notificationclick", (event) => {
  event.notification.close();
  event.waitUntil(
    clients.matchAll({ type: "window", includeUncontrolled: true }).then((list) => {
      for (const c of list) {
        if ("focus" in c) return c.focus();
      }
      if (clients.openWindow) return clients.openWindow("/");
    })
  );
});
