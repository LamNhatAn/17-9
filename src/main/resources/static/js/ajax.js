const state = { categories: [], products: [], loading: false };
const byId = id => document.getElementById(id);
const escapeHtml = value => String(value ?? '').replace(/[&<>'"]/g, character => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', "'": '&#39;', '"': '&quot;' }[character]));
const money = value => new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND', maximumFractionDigits: 0 }).format(value);

function notify(message, error = false) {
    const notice = byId('notice');
    notice.textContent = message;
    notice.className = `notice show${error ? ' error' : ''}`;
    clearTimeout(notify.timeout);
    notify.timeout = setTimeout(() => { notice.className = 'notice'; }, 3200);
}

async function rest(url, options = {}) {
    const response = await fetch(url, { headers: { 'Content-Type': 'application/json' }, ...options });
    if (!response.ok) throw new Error(await response.text() || 'Không thể hoàn thành thao tác.');
    return response.status === 204 ? null : response.json();
}

async function graphql(query, variables = {}) {
    const response = await fetch('/graphql', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ query, variables }) });
    const result = await response.json();
    if (result.errors) throw new Error(result.errors[0].message);
    return result.data;
}

function setLoading(loading) {
    state.loading = loading;
    document.querySelectorAll('button').forEach(button => { button.disabled = loading; });
}

function toggleEditor(id, visible, title) {
    byId(id).classList.toggle('hidden', !visible);
    if (title) byId(`${id.replace('-editor', '')}-editor-title`).textContent = title;
    if (visible) byId(id).scrollIntoView({ behavior: 'smooth', block: 'center' });
}

function resetProduct() { byId('product-form').reset(); byId('product-id').value = ''; toggleEditor('product-editor', false); }
function resetCategory() { byId('category-form').reset(); byId('category-id').value = ''; toggleEditor('category-editor', false); }

function renderCategories() {
    const selected = byId('product-category').value;
    byId('product-category').innerHTML = '<option value="">Tất cả danh mục</option>' + state.categories.map(category => `<option value="${category.id}">${escapeHtml(category.name)}</option>`).join('');
    byId('product-category').value = selected;
    byId('product-form-category').innerHTML = state.categories.map(category => `<option value="${category.id}">${escapeHtml(category.name)}</option>`).join('');
    byId('category-rows').innerHTML = state.categories.length ? state.categories.map(category => `<tr><td><strong>${escapeHtml(category.name)}</strong></td><td>${escapeHtml(category.description)}</td><td><div class="row-actions"><button onclick="editCategory(${category.id})">Sửa</button><button class="delete" onclick="deleteCategory(${category.id})">Xóa</button></div></td></tr>`).join('') : '<tr><td class="empty-row" colspan="3">Chưa có danh mục nào.</td></tr>';
    byId('category-count').textContent = state.categories.length;
}

function renderProducts() {
    byId('product-rows').innerHTML = state.products.length ? state.products.map(product => `<tr><td>${product.imageUrl ? `<img class="product-thumb" src="${escapeHtml(product.imageUrl)}" alt="Ảnh sản phẩm">` : ''}<strong>${escapeHtml(product.name)}</strong><br><small>${escapeHtml(product.description)}</small></td><td>${money(product.price)}</td><td>${product.quantity}</td><td>${escapeHtml(product.category?.name)}</td><td><div class="row-actions"><button onclick="editProduct(${product.id})">Sửa</button><button class="delete" onclick="deleteProduct(${product.id})">Xóa</button></div></td></tr>`).join('') : '<tr><td class="empty-row" colspan="5">Không tìm thấy sản phẩm phù hợp.</td></tr>';
    byId('product-count').textContent = state.products.length;
    byId('stock-count').textContent = state.products.reduce((total, product) => total + (product.quantity || 0), 0);
}

async function loadCategories() { state.categories = await rest('/api/categories'); renderCategories(); }
async function loadProducts() {
    const data = await graphql('query($keyword: String, $categoryId: ID) { products(keyword: $keyword, categoryId: $categoryId) { id name price quantity description imageUrl category { id name } } }', { keyword: byId('product-keyword').value.trim(), categoryId: byId('product-category').value || null });
    state.products = data.products;
    renderProducts();
}
async function refresh() { try { setLoading(true); await Promise.all([loadCategories(), loadProducts()]); notify('Dữ liệu đã được cập nhật.'); } catch (error) { notify(error.message, true); } finally { setLoading(false); } }

byId('product-search').onsubmit = async event => { event.preventDefault(); await loadProducts(); };
byId('product-category').onchange = loadProducts;
byId('product-keyword').oninput = (() => { let timer; return () => { clearTimeout(timer); timer = setTimeout(loadProducts, 300); }; })();
byId('refresh-products').onclick = refresh;
byId('show-product-form').onclick = () => { resetProduct(); toggleEditor('product-editor', true, 'Thêm sản phẩm'); };
byId('product-cancel').onclick = resetProduct;
byId('product-reset').onclick = resetProduct;
byId('product-form').onsubmit = async event => { event.preventDefault(); try { setLoading(true); const id = byId('product-id').value; const payload = { name: byId('product-name').value, price: Number(byId('product-price').value), quantity: Number(byId('product-quantity').value), description: byId('product-description').value, imageUrl: byId('product-image-url').value, category: { id: Number(byId('product-form-category').value) } }; const product = await rest(id ? `/api/products/${id}` : '/api/products', { method: id ? 'PUT' : 'POST', body: JSON.stringify(payload) }); const file = byId('product-image-file').files[0]; if (file && product?.id) { const formData = new FormData(); formData.append('file', file); const response = await fetch(`/api/products/${product.id}/image`, { method: 'POST', body: formData }); if (!response.ok) throw new Error(await response.text()); } resetProduct(); await loadProducts(); notify(id ? 'Đã cập nhật sản phẩm.' : 'Đã thêm sản phẩm.'); } catch (error) { notify(error.message, true); } finally { setLoading(false); } };
window.editProduct = id => { const product = state.products.find(item => item.id == id); if (!product) return; byId('product-id').value = product.id; byId('product-name').value = product.name; byId('product-price').value = product.price; byId('product-quantity').value = product.quantity; byId('product-description').value = product.description || ''; byId('product-image-url').value = product.imageUrl || ''; byId('product-form-category').value = product.category.id; toggleEditor('product-editor', true, 'Chỉnh sửa sản phẩm'); };
window.deleteProduct = async id => { if (!confirm('Xóa sản phẩm này?')) return; try { setLoading(true); await rest(`/api/products/${id}`, { method: 'DELETE' }); await loadProducts(); notify('Đã xóa sản phẩm.'); } catch (error) { notify(error.message, true); } finally { setLoading(false); } };

byId('show-category-form').onclick = () => { resetCategory(); toggleEditor('category-editor', true, 'Thêm danh mục'); };
byId('category-cancel').onclick = resetCategory;
byId('category-reset').onclick = resetCategory;
byId('category-form').onsubmit = async event => { event.preventDefault(); try { setLoading(true); const id = byId('category-id').value; const payload = { name: byId('category-name').value, description: byId('category-description').value }; await rest(id ? `/api/categories/${id}` : '/api/categories', { method: id ? 'PUT' : 'POST', body: JSON.stringify(payload) }); resetCategory(); await Promise.all([loadCategories(), loadProducts()]); notify(id ? 'Đã cập nhật danh mục.' : 'Đã thêm danh mục.'); } catch (error) { notify(error.message, true); } finally { setLoading(false); } };
window.editCategory = id => { const category = state.categories.find(item => item.id == id); if (!category) return; byId('category-id').value = category.id; byId('category-name').value = category.name; byId('category-description').value = category.description || ''; toggleEditor('category-editor', true, 'Chỉnh sửa danh mục'); };
window.deleteCategory = async id => { if (!confirm('Xóa danh mục này?')) return; try { setLoading(true); await rest(`/api/categories/${id}`, { method: 'DELETE' }); await Promise.all([loadCategories(), loadProducts()]); notify('Đã xóa danh mục.'); } catch (error) { notify(error.message, true); } finally { setLoading(false); } };

refresh();
