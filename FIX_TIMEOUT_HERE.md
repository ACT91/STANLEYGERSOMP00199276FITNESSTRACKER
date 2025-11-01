# 🚨 FIXING YOUR CONNECTION TIMEOUT

## **Your Error:**
```
SocketTimeoutException: failed to connect to /192.168.114.222
```

## **The Problem:**
Your app can't reach the server because **Windows Firewall is blocking port 80**.

## **The Solution (30 seconds):**

### 1️⃣ Run This File (Right-click → Run as Administrator):
```
fix_network.bat
```

### 2️⃣ Make Sure Apache is Running:
- Open **XAMPP Control Panel**
- Apache should show **green "Running"**
- If not, click **Start**

### 3️⃣ Test on Phone Browser:
Go to: `http://192.168.114.222/fitness_tracker_api/`

- ✅ See dashboard? → **Try app again!**
- ❌ Timeout? → **Read detailed guides below**

---

## 📚 Detailed Guides (If Quick Fix Doesn't Work):

| File | What It Does |
|------|-------------|
| **CONNECTION_TIMEOUT_FIX.md** | Quick reference guide |
| **VISUAL_FIX_GUIDE.md** | Step-by-step with diagrams |
| **NETWORK_TROUBLESHOOTING.md** | Complete troubleshooting |
| **test_network.bat** | Diagnose connection issues |
| **fix_network.bat** | Automatically fix firewall |

---

## 🎯 Most Common Issues:

1. **Windows Firewall blocking** (90% of cases)
   - **Fix:** Run `fix_network.bat` as Administrator

2. **Apache not running**
   - **Fix:** Start Apache in XAMPP Control Panel

3. **Not on same Wi-Fi**
   - **Fix:** Connect phone and PC to same network

4. **Wrong IP address**
   - **Fix:** Run `ipconfig` to verify IP, update in app

---

## ✅ How to Know It's Fixed:

### Before Fix (Current State):
```
Logcat shows:
❌ SocketTimeoutException after 30000ms
```

### After Fix:
```
Logcat shows:
✅ <-- 200 OK
   {"status":"success", "message":"User registered successfully"}

OR even:
✅ <-- 409 Conflict
   {"status":"error", "message":"Email already registered"}
```

**Any HTTP response (even errors) means connection works!**

---

## 🚀 START HERE:

1. **Right-click** `fix_network.bat`
2. Select **"Run as administrator"**
3. Wait for success message
4. **Test app**

**That's it!** 🎉

If still not working, read the detailed guides.

---

**TL;DR:** Windows Firewall is blocking your connection. Run `fix_network.bat` as Admin to fix it!

