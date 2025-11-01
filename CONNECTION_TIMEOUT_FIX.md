# 🚨 CONNECTION TIMEOUT - QUICK FIX

## Your Error:
```
SocketTimeoutException: failed to connect to /192.168.114.222 (port 80) 
from /192.168.114.124 after 30000ms
```

---

## ⚡ FASTEST FIX (2 Minutes)

### Step 1: Add Firewall Rule
**Right-click** `fix_network.bat` → **Run as administrator**

This automatically adds a Windows Firewall rule to allow Apache.

### Step 2: Verify Apache is Running
1. Open **XAMPP Control Panel**
2. Make sure **Apache** shows **green "Running"**
3. If not, click **Start**

### Step 3: Test Connection
**On your phone's browser**, go to:
```
http://192.168.114.222/fitness_tracker_api/
```

**If you see the API dashboard:** ✅ FIXED! Try app again.

**If timeout/error:** Continue to detailed fix below.

---

## 🔧 DETAILED FIX (If Quick Fix Doesn't Work)

### Issue #1: Windows Firewall Blocking (90% of cases)

**Temporary Test:**
1. Open **Windows Security** (search in Start menu)
2. Click **Firewall & network protection**
3. Click your active network (usually "Private network")
4. Toggle **OFF** Windows Defender Firewall
5. **Test app immediately**

**If app works now:**
- Firewall was the issue! ✅
- Turn firewall back ON
- Run `fix_network.bat` as Administrator to add permanent rule

**Permanent Fix Command (as Administrator):**
```cmd
netsh advfirewall firewall add rule name="Apache HTTP" dir=in action=allow protocol=TCP localport=80
```

---

### Issue #2: Not on Same Wi-Fi Network

**Check on PC:**
1. Open Command Prompt
2. Type: `ipconfig`
3. Find IPv4 Address (e.g., 192.168.114.222)

**Check on Phone:**
1. Settings → Wi-Fi
2. Tap connected network
3. Check IP address (e.g., 192.168.114.124)

**Both must be in same range:**
- ✅ Good: 192.168.114.xxx
- ❌ Bad: Different ranges (e.g., 192.168.1.xxx vs 192.168.114.xxx)

**Fix:** Connect both to the **same Wi-Fi network**

---

### Issue #3: Wrong IP Address

**Your current IP: 192.168.114.222**

This might have changed. To verify:

1. On PC, run: `ipconfig`
2. Look for **IPv4 Address** under your Wi-Fi adapter
3. If different from 192.168.114.222, update your app:

**Update in:** `RetrofitClient.kt`
```kotlin
private const val BASE_URL = "http://YOUR_NEW_IP/fitness_tracker_api/api/"
```

---

### Issue #4: Apache Not Configured for Network Access

Edit: `C:\xampp\apache\conf\extra\httpd-xampp.conf`

Find:
```apache
Require local
```

Add below it:
```apache
Require ip 192.168.114
```

**Restart Apache** in XAMPP Control Panel.

---

## 📱 TEST FROM PHONE BROWSER FIRST

**Before testing app, test in phone browser:**

```
http://192.168.114.222/
```
Should show: XAMPP dashboard

```
http://192.168.114.222/fitness_tracker_api/
```
Should show: Fitness Tracker API dashboard

```
http://192.168.114.222/fitness_tracker_api/setup.php
```
Should show: Database setup page

**If ALL work in browser but app still times out:**
- App configuration issue
- Try rebuilding the app
- Clear app cache

**If NONE work in browser:**
- Firewall blocking (most likely)
- Apache not running
- Not on same network

---

## 🎯 VERIFICATION

Run this to test everything:
```
Double-click: test_network.bat
```

This will:
- ✅ Test if server is reachable
- ✅ Check if Apache is running
- ✅ Verify firewall rule exists
- ✅ Test API accessibility

---

## ✅ CHECKLIST

- [ ] XAMPP Apache is running (green light)
- [ ] Phone and PC on same Wi-Fi
- [ ] Firewall allows port 80
- [ ] Can access http://192.168.114.222/ from phone browser
- [ ] Can access http://192.168.114.222/fitness_tracker_api/ from phone browser
- [ ] IP address in app matches PC's actual IP

---

## 🆘 STILL NOT WORKING?

### Alternative Method: USB + ADB Port Forwarding

1. Connect phone via USB cable
2. Enable USB Debugging on phone
3. On PC, run:
   ```cmd
   adb reverse tcp:80 tcp:80
   ```
4. Change app BASE_URL to:
   ```kotlin
   private const val BASE_URL = "http://localhost/fitness_tracker_api/api/"
   ```
5. Rebuild and run app

This routes phone's localhost to PC's localhost through USB.

---

## 📞 COMMANDS SUMMARY

```bash
# Check your IP
ipconfig

# Add firewall rule (run as Administrator)
netsh advfirewall firewall add rule name="Apache HTTP" dir=in action=allow protocol=TCP localport=80

# Test API from PC
curl http://192.168.114.222/fitness_tracker_api/

# ADB port forwarding (if using USB)
adb reverse tcp:80 tcp:80
```

---

## 🎉 SUCCESS INDICATOR

When fixed, you'll see in Logcat:
```
<-- 200 OK http://192.168.114.222/fitness_tracker_api/api/register.php
Content-Type: application/json
{
  "status": "success",
  "message": "User registered successfully"
}
```

OR at minimum (even if error from API):
```
<-- 409 Conflict
{
  "status": "error",
  "message": "Email already registered"
}
```

**Any HTTP response = connection works!** (vs timeout = connection blocked)

---

## 📁 HELPER SCRIPTS

- **fix_network.bat** - Adds firewall rule (run as Admin)
- **test_network.bat** - Tests all connection points
- **NETWORK_TROUBLESHOOTING.md** - Detailed guide

---

**90% of connection issues = Windows Firewall blocking port 80!**

**Fix:** Run `fix_network.bat` as Administrator → Problem solved! 🚀

