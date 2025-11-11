# 🔧 NETWORK CONNECTIVITY TROUBLESHOOTING GUIDE

## ❌ PROBLEM: SocketTimeoutException - Cannot Connect to Server

Your app is correctly configured but cannot reach the XAMPP server on your PC.

**Error:** `failed to connect to /192.168.114.222 (port 80) from /192.168.114.124 after 30000ms`

---

## ✅ SOLUTION STEPS (Follow in Order)

### STEP 1: Verify XAMPP Apache is Running

1. Open **XAMPP Control Panel**
2. Check Apache shows **green "Running"** status
3. If not running, click **Start** next to Apache

**Test:** Open browser on your PC → http://localhost
- Should see XAMPP dashboard ✅

---

### STEP 2: Test API on Your PC First

Open browser on your **PC** (not phone):

```
http://192.168.114.222/fitness_tracker_api/
```

**Expected Result:** Should see the API dashboard

**If this fails:**
- Apache is not running OR
- Files not deployed correctly

**Fix:**
```
1. Run: setup_complete.bat
2. OR manually copy php_api to C:\xampp\htdocs\fitness_tracker_api\
3. Restart Apache in XAMPP
```

---

### STEP 3: Check Windows Firewall (CRITICAL!)

**This is the most common issue!** Windows Firewall blocks external connections.

#### Option A: Quick Test (Temporary)
1. Open **Windows Security**
2. Click **Firewall & network protection**
3. Click your active network (Private/Public)
4. **Turn OFF** "Windows Defender Firewall" temporarily
5. Test app again

**If it works now:** Firewall was blocking it!

#### Option B: Permanent Fix (Recommended)
Create a firewall rule to allow Apache:

1. Open **Windows Defender Firewall with Advanced Security**
   - Press Windows Key + R
   - Type: `wf.msc`
   - Press Enter

2. Click **Inbound Rules** → **New Rule...**

3. Select **Port** → Next

4. Select **TCP**, Specific local ports: `80` → Next

5. Select **Allow the connection** → Next

6. Check all boxes (Domain, Private, Public) → Next

7. Name: `Apache HTTP Server` → Finish

8. Test app again

---

### STEP 4: Verify Both Devices on Same Network

**On your PC:**
```
1. Press Windows Key + R
2. Type: cmd
3. Press Enter
4. Type: ipconfig
5. Look for "IPv4 Address" under your active adapter
   Example: 192.168.114.222
```

**On your phone:**
```
1. Settings → Wi-Fi
2. Tap your connected network
3. Check IP address
   Example: 192.168.114.124
```

**Both should be in same range:**
- ✅ Good: 192.168.114.xxx (same subnet)
- ❌ Bad: Different subnets (e.g., 192.168.1.xxx vs 192.168.114.xxx)

**If different:**
- Make sure phone and PC are on **same Wi-Fi network**
- Disconnect and reconnect phone to Wi-Fi

---

### STEP 5: Test Direct Connection from Phone

**On your phone's browser**, go to:
```
http://192.168.114.222/
```

**Expected:** XAMPP dashboard

**If you see XAMPP dashboard:**
✅ Connection works! Now test API:
```
http://192.168.114.222/fitness_tracker_api/
```

**If timeout/cannot connect:**
- Firewall is blocking (see Step 3)
- OR Apache not configured for network access (see Step 6)

---

### STEP 6: Configure Apache for Network Access

Edit Apache configuration to allow connections from your network:

1. Open file: `C:\xampp\apache\conf\extra\httpd-xampp.conf`

2. Find this section:
```apache
<LocationMatch "^/(?i:(?:xampp|security|licenses|phpmyadmin|webalizer|server-status|server-info))">
```

3. Look for:
```apache
Require local
```

4. **Add this line below it:**
```apache
Require ip 192.168.114
```

5. The section should look like:
```apache
<LocationMatch "^/(?i:(?:xampp|security|licenses|phpmyadmin|webalizer|server-status|server-info))">
    Require local
    Require ip 192.168.114
    ErrorDocument 403 /error/XAMPP_FORBIDDEN.html.var
</LocationMatch>
```

6. **Save file**

7. **Restart Apache** in XAMPP Control Panel

---

### STEP 7: Test with cURL (Advanced)

**On your PC**, open Command Prompt:

```bash
curl -X POST http://192.168.114.222/fitness_tracker_api/api/register.php ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"test@example.com\",\"password\":\"password123\",\"name\":\"Test\",\"age\":25,\"weight\":70,\"height\":175}"
```

**Expected:** JSON response with success or error

**If this works but phone doesn't:**
- Firewall issue (see Step 3)

---

### STEP 8: Check Antivirus Software

Some antivirus software blocks incoming connections:

1. Check if you have antivirus (Norton, McAfee, Avast, etc.)
2. Temporarily disable it
3. Test app
4. If works, add Apache/XAMPP to antivirus exceptions

---

### STEP 9: Alternative - Use Different IP

Sometimes the detected IP isn't correct. Try:

**Find all IPs on your PC:**
```bash
ipconfig /all
```

Look for different IPv4 addresses and try each in your app.

---

## 🔥 QUICK FIX (Most Common Solution)

**90% of the time, the issue is Windows Firewall!**

### The Fastest Fix:

1. **Temporarily disable Windows Firewall:**
   - Windows Security → Firewall → Turn OFF

2. **Test app** - Should work now!

3. **Re-enable Firewall**

4. **Add permanent rule:**
   - Run this in **Command Prompt (as Administrator)**:
   ```cmd
   netsh advfirewall firewall add rule name="Apache HTTP" dir=in action=allow protocol=TCP localport=80
   ```

5. **Done!** Firewall now allows Apache on port 80

---

## 📱 UPDATE APP CONFIGURATION

After confirming the correct IP, update your app:

**File:** `RetrofitClient.kt`

**Current:**
```kotlin
private const val BASE_URL = "http://192.168.114.222/fitness_tracker_api/api/"
```

**Make sure it matches your actual IP!**

---

## ✅ VERIFICATION CHECKLIST

After fixing, verify:

- [ ] XAMPP Apache is running (green)
- [ ] Can access http://192.168.114.222/ from PC browser
- [ ] Can access http://192.168.114.222/ from phone browser
- [ ] Can access http://192.168.114.222/fitness_tracker_api/ from phone browser
- [ ] Windows Firewall allows port 80
- [ ] Phone and PC on same Wi-Fi network
- [ ] App shows success/error message (not timeout)

---

## 🎯 EXPECTED RESULT

After fixing, you should see in Logcat:

```
<-- 200 OK (response time)
Content-Type: application/json
{
  "status": "success",
  "message": "User registered successfully",
  "data": { ... }
}
```

OR an error from the API (not a timeout):

```
<-- 409 Conflict
{
  "status": "error",
  "message": "Email already registered"
}
```

**Any response is good** - it means connection works!

---

## 🆘 STILL NOT WORKING?

### Try USB Debugging + ADB Port Forwarding:

1. Connect phone via USB
2. Enable USB Debugging on phone
3. On PC, open Command Prompt:
   ```cmd
   adb reverse tcp:80 tcp:80
   ```
4. In app, change BASE_URL to:
   ```kotlin
   private const val BASE_URL = "http://localhost/fitness_tracker_api/api/"
   ```
5. This routes phone's localhost to PC's localhost

---

## 📝 SUMMARY

**Most Common Issues (in order):**

1. **Windows Firewall blocking** (90% of cases) → Disable temporarily or add rule
2. **Not on same network** → Connect both to same Wi-Fi
3. **Apache not running** → Start in XAMPP
4. **Wrong IP address** → Use `ipconfig` to find correct IP
5. **Antivirus blocking** → Add exception

**Follow Steps 1-6 and you should be connected!** 🚀

---

**Quick Command Reference:**

```bash
# Find your IP
ipconfig

# Add firewall rule (as Administrator)
netsh advfirewall firewall add rule name="Apache HTTP" dir=in action=allow protocol=TCP localport=80

# Test API from PC
curl http://192.168.114.222/fitness_tracker_api/api/register.php
```

---

**Need More Help?**

Check the logs:
- Apache error log: `C:\xampp\apache\logs\error.log`
- PHP error log: `C:\xampp\php\logs\php_error_log`

