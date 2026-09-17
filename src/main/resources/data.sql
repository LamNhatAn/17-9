INSERT INTO categories (name, description) VALUES
('Laptop', 'Máy tính xách tay cho học tập và công việc'),
('Điện thoại', 'Thiết bị di động hỗ trợ kết nối và giải trí'),
('Tai nghe', 'Thiết bị âm thanh cá nhân'),
('Máy ảnh', 'Thiết bị chụp hình chuyên nghiệp'),
('Phụ kiện', 'Các thiết bị hỗ trợ và tiện ích đi kèm'),
('Tivi', 'Màn hình giải trí gia đình'),
('Máy in', 'Thiết bị in ấn văn phòng'),
('Đồng hồ', 'Thiết bị theo dõi thời gian và phong cách'),
('Bàn phím', 'Thiết bị nhập liệu máy tính'),
('Chuột', 'Thiết bị điều khiển máy tính');

INSERT INTO products (name, price, quantity, description, image_url, category_id) VALUES
('Laptop Lenovo IdeaPad', 15990000, 12, 'Laptop học tập và làm việc', 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?auto=format&fit=crop&w=160&q=80', 1),
('iPhone 15', 18990000, 8, 'Điện thoại thông minh Apple', 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?auto=format&fit=crop&w=160&q=80', 2),
('Tai nghe Sony WH-1000XM5', 7490000, 15, 'Tai nghe chống ồn chủ động', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=160&q=80', 3),
('Máy ảnh Canon EOS R50', 16990000, 5, 'Máy ảnh mirrorless cho người mới bắt đầu', 'https://images.unsplash.com/photo-1516035069371-29a1b244cc32?auto=format&fit=crop&w=160&q=80', 4),
('Bàn phím cơ Keychron K2', 2190000, 20, 'Bàn phím cơ không dây', 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?auto=format&fit=crop&w=160&q=80', 9),
('Chuột Logitech MX Master 3S', 1990000, 18, 'Chuột không dây công thái học', 'https://images.unsplash.com/photo-1527814050087-3793815479db?auto=format&fit=crop&w=160&q=80', 10);
