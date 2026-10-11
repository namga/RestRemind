# Hướng dẫn build app bằng PWABuilder

## Bước 1 – Host PWA (bắt buộc)

1. Vào https://app.netlify.com/drop
2. Kéo thả TOÀN BỘ file trong thư mục này (không bọc thêm folder)
3. Copy URL dạng: https://xxxx.netlify.app

Kiểm tra:
- https://xxxx.netlify.app/manifest.json  → hiện JSON
- https://xxxx.netlify.app/sw.js          → hiện code JS

## Bước 2 – PWABuilder

1. Mở https://www.pwabuilder.com
2. Dán URL Netlify → Start
3. Manifest và Service Worker phải hiện màu xanh
4. Package for stores → Android / iOS / Windows

## Lưu ý

- Không upload zip trực tiếp vào ô URL của PWABuilder
- Không mở bằng file://
- Icon 512x512 đã có sẵn trong gói
