/**
 * 日期格式化工具函数
 */

/**
 * 将日期格式化为 YYYY-MM-DD 格式的字符串
 * @param {Date|string} date - 要格式化的日期对象或日期字符串
 * @returns {string} 格式化后的日期字符串
 */
export function formatDate(date) {
  if (!date) return '';
  
  // 如果传入的是字符串，尝试转换为日期对象
  if (typeof date === 'string') {
    // 如果已经是 YYYY-MM-DD 格式，直接返回
    if (/^\d{4}-\d{2}-\d{2}$/.test(date)) {
      return date;
    }
    date = new Date(date);
  }
  
  // 检查是否是有效日期
  if (isNaN(date.getTime())) {
    return '';
  }
  
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  
  return `${year}-${month}-${day}`;
}

/**
 * 将日期格式化为 YYYY-MM-DD HH:MM:SS 格式的字符串
 * @param {Date|string} date - 要格式化的日期对象或日期字符串
 * @returns {string} 格式化后的日期时间字符串
 */
export function formatDateTime(date) {
  if (!date) return '';
  
  // 如果传入的是字符串，转换为日期对象
  if (typeof date === 'string') {
    date = new Date(date);
  }
  
  // 检查是否是有效日期
  if (isNaN(date.getTime())) {
    return '';
  }
  
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

/**
 * 获取当前日期的字符串表示（YYYY-MM-DD）
 * @returns {string} 当前日期的字符串
 */
export function getCurrentDate() {
  return formatDate(new Date());
}

/**
 * 获取当前日期时间的字符串表示（YYYY-MM-DD HH:MM:SS）
 * @returns {string} 当前日期时间的字符串
 */
export function getCurrentDateTime() {
  return formatDateTime(new Date());
}