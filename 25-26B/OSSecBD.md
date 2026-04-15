**ШИНЖЛЭХ УХААН ТЕХНОЛОГИЙН ИХ СУРГУУЛЬ**  
**Мэдээлэл Холбооны Технологийн Сургууль**

![][image1]

# **БИЕ ДААЛТЫН АЖЛЫН** 

# **ТАЙЛАН**

Үйлдлийн систем ба аюулгүй байдал (F.NSA202)  
2025-2026B оны хичээлийн жилийн хаврын улирал

Бие даалтын ажлын нэр: 		Linux файлын системийн   
зөвшөөрлийн загвар ба аюулгүй  
байдлын хэрэгжилтийн судалгаа  
Хичээл заасан багш: 			Б.Ундрал  
Бие даалтыг гүйцэтгэсэн: 		Э.Мөнх-Оргил /B241870008/  
					Э.Бат-Өлзий /B241870011/

| Авбал зохих нийт оноо: | 10 оноо |  |
| ----- | :---: | :---: |
| **Гүйцэтгэлийн шалгуур** | **Үнэлгээний эзлэх хувь** | **Багшийн үнэлгээ** |
| Сэдвийн онолын ойлголт | 20% |  |
| Аюулгүй байдлын холбоог тайлбарласан байдал | 20% |  |
| Жишээ, баримт, зураг, схем ашигласан байдал | 15% |  |
| Илтгэлийн бүтэц, слайдын чанар | 15% |  |
| Илтгэх чадвар, тайлбарлах чадвар | 20% |  |
| Үнэлгээний нийт хувь | **100%** |  |
| Нийт авсан оноо |  |  |

Улаанбаатар   
2026 он

---
title: Linux файлын системийн зөвшөөрлийн загвар ба аюулгүй байдлын хэрэгжилт
subtitle: Үйлдлийн систем ба аюулгүй байдал (F.NSA202) - Бие даалтын тайлан
author: Э.Мөнх-Оргил (B241870008), Э.Бат-Өлзий (B241870011)
date: 2026
subject: Operating Systems and Security
---

# i. Тойм {#i.-тойм}

## Товчоон

Энэ тайлан нь Linux операцийн системийн файлын системийн зөвшөөрлийн загвар (File System Permissions Model) болон аюулгүй байдлын хэрэгжилтийн талаарх судалгааг агуулна. Linux-ын файлын системийн зөвшөөрлийн загвар нь UNIX-ийн аюулгүй байдлын эхлэлийн нэг юм. Энэ загвар нь эзэмшигч (owner), бүлэг (group), бусад хэрэглэгчид (others) гүйцэтгэх эрх (permissions) -ын хооронд ялгавар гаргадаг. 

Тайланд үзүүлэх үл агуулаа:
- Файлын системийн зөвшөөрлийн үндсэн ойлголтууд
- Unix файлын системийн аюулгүй байдлын архитектур
- Практик жишээ ба команд-ын байршил
- Аюулгүйд байдлын эрсдэл ба хорлон
- Хамгаалах арга ба сайн үйлдлүүд (Best Practice)

---

# ii. Гарчиг {#ii.-гарчиг}

[i. Тойм](#i.-тойм)

[ii. Гарчиг](#ii.-гарчиг)

[1. Сэдвийн танилцуулга](#сэдвийн-танилцуулга)
- [1.1 Яагаад чухал вэ? (Ач холбогдол)](#яагаад-чухал-вэ-ач-холбогдол)
- [1.2 Ямар үйлдлийн системтэй холбоотой вэ?](#ямар-үйлдлийн-системтэй-холбоотой-вэ)

[2. Үндсэн ойлголт](#үндсэн-ойлголт)
- [2.1 Гол нэр томьёо](#гол-нэр-томьёо)
- [2.2 Ажиллах зарчим](#ажиллах-зарчим)
- [2.3 Архитектур, бүрэлдэхүүн хэсэг](#архитектур-бүрэлдэхүүн-хэсэг)

[3. Аюулгүй байдлын холбоо](#аюулгүй-байдлын-холбоо)
- [3.1 OS security-д үзүүлэх нөлөө](#os-security-д-үзүүлэх-нөлөө)
- [3.2 Эрсдэл, эмзэг байдал](#эрсдэл-эмзэг-байдал)

[4. Жишээ ба практик ашигласан байдал](#жишээ)

[5. Давуу ба сул тал](#давуу-ба-сул-тал)

[6. Хамгаалах арга / зөв хэрэглээ](#хамгаалах-арга-зөв-хэрэглээ)

[7. Дүгнэлт](#дүгнэлт)

[8. Ашигласан эх сурвалж](#ашигласан-эх-сурвалж)

---

---

## Хичээлийн заавал оруулах бүтэц ба шаардлага

| Элемент | Шаардлага |
|---------|-----------|
| Даалгавар | 15 сэдвээс 1 сэдэв сонгож, судалгаа хийх |
| Үйлдвэрлэл | PowerPoint илтгэл бэлтгэх |
| Илтгэлийн хугацаа | Лабораторийн цаг дээр 8–10 минут |
| Шалгалтын хугацаа | 12-14-р 7 хоног |
| Баг дэх гишүүнийн хамгийн их тоо | 5 гишүүн |
| Тайланы эзлэхүүн | 15–20 хуудас |

### Зорилго

Сонгосон сэдвийн онолын үндэс, үйлдлийн системийн аюулгүй байдалтай холбоо, бодит хэрэглээ, эрсдэл, хамгаалах аргыг ойлгож тайлбарлах.

### Заавал оруулах элементүүд
- Ажиллах зарчим
- Аюулгүй байдлын эрсдэл
- Бодит жишээ
- Хамгаалах арга

---

# 1. Сэдвийн танилцуулга {#сэдвийн-танилцуулга}

## 1.1 Яагаад чухал вэ? (Ач холбогдол) {#яагаад-чухал-вэ-ач-холбогдол}

Linux операцийн системийн файлын системийн зөвшөөрлийн загвар (File System Permissions Model) нь аюулгүйд байдлын үндсэн хэсэг юм. Энэ загвар нь:

1. **Мультисистемийн ашигласагч хүүхэлсүүлэх (Multi-user System Protection)**
   - Олон хэрэглэгч нэг үйлдлийн систем ашиглаж байх үед тус бүрийн өгөгдлийг нэг нөгөөгөөс хамгаалдаг
   - Системийн файлуудыг энгийн хэрэглэгчээс хамгаалдаг

2. **Өргөнт засав ба удирдлага (Access Control and Management)**
   - Файлууд ба директорийг хэн нь унших, өөрчлөх, гүйцэтгэх боломжтойг удирдаж чаддаг
   - Эмзэг шалтгаалсан мэдээлэлийг хэнээс нүүцлэх хэрэгтэйг тодорхойлоход сонголт өгдөг

3. **Системийн бүрэн байдал (System Integrity)**
   - Эх, гэм нийгмийнхөө админ функцийг эрх баримтгүй өөрчилж чадахгүй гэж хамгаалдаг
   - Чухал системийн файлуудыг зохиомжтой эсвэл сохор өөрчилтээс хамгаалдаг

4. **Аюулгүй байдлын шаардлагын нийцүүлэлт (Security Compliance)**
   - Үйлдвэрлэлийн үйл ажиллагаанд шаардлагатай мэдээлэл нууцлалын стандартуудыг (ISO 27001, PCI-DSS) туршдаг
   - Эрх зүйн өгөгдлийн аюулгүй байдлын шаардлагыг (GDPR, HIPAA) дээж чиглүүлдэг

## 1.2 Ямар үйлдлийн системтэй холбоотой вэ? {#ямар-үйлдлийн-системтэй-холбоотой-вэ}

Linux файлын системийн зөвшөөрлийн загвар нь:

- **Linux Operating System (Ubuntu, CentOS, Fedora, Debian)**
  - Амжилтаа UNIX-ийн философиас авсан
  - Бүх Linux дистрибьюцид нэгдэн ажиллаж байгаа

- **UNIX болон UNIX-юм системүүд**
  - macOS (BSD суурьтай)
  - FreeBSD, OpenBSD
  - Solaris

- **Сервер ба хосттай систем**
  - Web серверүүд (Apache, Nginx)
  - Датабазын серверүүд (MySQL, PostgreSQL)
  - Файл серверүүд ба нэгдлийн хүлээлтийн системүүд

---

# 2. Үндсэн ойлголт {#үндсэн-ойлголт}

## 2.1 Гол нэр томьёо {#гол-нэр-томьёо}

### Үндсэн нэр томьёонууд

| Нэр томьёо | Англи нэр | Тайлбар |
|-----------|-----------|--------|
| **Хэрэглэгч (User)** | User/UID | Системийг ашигладаг хүн эсвэл процесс |
| **Бүлэг (Group)** | Group/GID | Нэг ба түнгийн хэрэглэгчдийн цуглуулга |
| **Эзэмшигч (Owner)** | Owner/UID | Файл эсвэл директорийг үүсгэсэн хэрэглэгч |
| **Зөвшөөрөл (Permission)** | Permission | Файл эсвэл директоригийн хүүхэлсүүлэх хүүхэлсүүлэх утга |
| **Унших эрх (Read)** | r / 4 | Файлын агуулгыг уршдах боломж |
| **Бичих эрх (Write)** | w / 2 | Файлыг өөрчилж, үүсгэх боломж |
| **Гүйцэтгэх эрх (Execute)** | x / 1 | Файлыг програм гэж гүйцэтгэх боломж |
| **Эзэмшигчийн эрх** | Owner/User | Файл ба директорийн эзэмшигчийн эрхүүд |
| **Бүлгийн эрх** | Group | Бүлгийн гишүүдийн эрхүүд |
| **Бусадын эрх** | Others | Бүлгийн эзэмшигч болон бүлэгт хамааралгүй хэрэглэгчдийн эрхүүд |

### Нэмэлт ойлголтууд

- **Суперхэрэглэгч (Root/Superuser)**: UID 0-той хэрэглэгч, бүх зөвшөөрөл эрхтэй
- **Системийн хэрэглэгч (System User)**: Системийн үйлчилгээ ажиллуулах хэрэглэгч (nginx, mysql г.м.)
- **Дефолт зөвшөөрөл (Default Permissions/umask)**: Шинэ файл үүсгэхэд автоматаар хэрэглэгдэх зөвшөөрөл

## 2.2 Ажиллах зарчим {#ажиллах-зарчим}

### Зөвшөөрлийн бүтэц

Linux файлын системийн зөвшөөрөл нь 10 төрлийн тэмдэгтээс бүрдэнэ:

```
-rw-r--r--
^ ^^^^^^^^
| └── Зөвшөөрөл (9 character)
└── Файлын төрөл (1 character)
```

**Файлын төрөл (1-р тэмдэгт):**
- `-` : Энгийн файл
- `d` : Директори
- `l` : Холбоос (Symbolic Link)
- `s` : Socket
- `p` : Pipe
- `b` : Block device
- `c` : Character device

**Зөвшөөрөл (2-10 тэмдэгт):**

```
rw-r--r--
└─┬─┘└─┬──┘└─┬─┘
  │    │     └─ Бусад (Others): r--
  │    └─────── Бүлэг (Group): r--
  └──────────── Эзэмшигч (Owner): rw-
```

### Эрхийн утга

- **r (Read) = 4** : Уршдах эрх
- **w (Write) = 2** : Бичих эрх
- **x (Execute) = 1** : Гүйцэтгэх эрх

### Тоон утга илэрхийлэл

Тоон утганы жишээ:

| Эрхүүд | Тоо | Тайлбар |
|------|-----|--------|
| `rwx` | 7 | Бүх эрхүүд (4+2+1) |
| `rw-` | 6 | Унших ба бичих эрхүүд (4+2) |
| `r-x` | 5 | Унших ба гүйцэтгэх эрхүүд (4+1) |
| `r--` | 4 | Зөвхөн унших эрхүүд |
| `-wx` | 3 | Бичих ба гүйцэтгэх эрхүүд (2+1) |
| `-w-` | 2 | Зөвхөн бичих эрхүүд |
| `--x` | 1 | Зөвхөн гүйцэтгэх эрхүүд |
| `---` | 0 | Эрхгүй |

**Жишээ:** `chmod 755 file.sh`
- Эзэмшигч: 7 (rwx) - Бүх эрхүүд
- Бүлэг: 5 (r-x) - Унших ба гүйцэтгэх
- Бусад: 5 (r-x) - Унших ба гүйцэтгэх

## 2.3 Архитектур, бүрэлдэхүүн хэсэг {#архитектур-бүрэлдэхүүн-хэсэг}

### Файлын зөвшөөрлийн архитектур

```
┌─────────────────────────────────────┐
│    Linux Operating System          │
├─────────────────────────────────────┤
│                                     │
│  ┌────────────────────────────┐    │
│  │   User Space (Хэрэглэгч)   │    │
│  │                            │    │
│  │  User Process ──┐         │    │
│  │  (UID, GID)     │         │    │
│  └────────────────│──────────┘    │
│                   │                │
│  ┌────────────────▼──────────┐    │
│  │  Kernel Space (Цөмөрс)     │    │
│  │                            │    │
│  │ • File System Driver       │    │
│  │ • Permission Check         │    │
│  │ • Access Control List (ACL)│    │
│  └────────────┬───────────────┘    │
│               │                     │
│  ┌────────────▼──────────────┐    │
│  │   File System             │    │
│  │  (ext4, XFS, Btrfs)       │    │
│  │                            │    │
│  │  Inode: Owner, Group,     │    │
│  │         Permissions        │    │
│  └────────────────────────────┘    │
│                                     │
└─────────────────────────────────────┘
```

### Inode структур

Файл бүр inode нэмэлт мэдээлэл агуулдаг:

```c
struct inode {
    uid_t    i_uid;        // Эзэмшигчийн ID
    gid_t    i_gid;        // Бүлгийн ID
    mode_t   i_mode;       // Файлын төрөл ба зөвшөөрөл
    time_t   i_atime;      // Сүүлийн нэвтрэлтийн цаг
    time_t   i_mtime;      // Сүүлийн өөрчлөлтийн цаг
    time_t   i_ctime;      // Сүүлийн цагийн өөрчлөлт
    // ... бусад мэдээлэл
};
```

### Зөвшөөрлийн шалгалтын процесс

```
Процесс файл ашиглахыг оролдов (UID=1000)
         │
         ▼
    ┌─────────────────────────────┐
    │ Процессийн UID == Файлын    │
    │ эзэмшигч UID?               │
    └──────┬──────────────────┬───┘
         ДА │                 │ ҮҮ
           │                 │
      ┌────▼─────┐       ┌───▼──────────────┐
      │ Эзэмшигчийн│       │ Процессийн GID  │
      │  эрхүүд    │       │ == Файлын бүлэг │
      │ ашигла     │       │ GID?            │
      └────┬─────┘       └───┬──────┬───────┘
           │                 ДА │   │ ҮҮ
           │                    │   │
           │             ┌──────▼───▼──────┐
           │             │ Бүлгийн эрхүүд  │
           │             │ ашигла          │
           │             └──────┬──────────┘
           │                    │
           ▼                    ▼
      ┌──────────────────────────────────┐
      │ Бусадын эрхүүд ашигла (Others)  │
      └──────────────────────────────────┘
```

---

# 3. Аюулгүй байдлын холбоо {#аюулгүй-байдлын-холбоо}

## 3.1 OS security-д үзүүлэх нөлөө {#os-security-д-үзүүлэх-нөлөө}

### Аюулгүй байдлын үндсэн 3 зарчим (CIA Triad)

Linux файлын системийн зөвшөөрлийн загвар нь CIA үндсэн зарчимд нөлөөлдөг:

1. **Нууцлал (Confidentiality)**
   - Файлын ерөнхий зөвшөөрөл нь нууцлалыг хамгаалдаг
   - Жишээ: `chmod 600 secret.txt` гэвэл зөвхөн эзэмшигч уршдах боломжтой
   - Уршдах эрхгүй хэрэглэгч файлын агуулгыг мэдэх боломжгүй

2. **Бүрэн байдал (Integrity)**
   - Бичих эрхийн хамгаалалт нь өгөгдлийн өөрчлөлтөөс хамгаалдаг
   - Жишээ: `chmod 444 important.conf` гэвэл хэн ч файлыг өөрчилж чадахгүй (root эс тэгвэл)
   - Шалтгаалтай өөрчлөлтүүд (Unauthorized Modifications) урьдчилан сэргийлдэг

3. **Бэлэн байдал (Availability)**
   - Гүйцэтгэх эрхийн зохих удирдлага системийн үйлчилгээнүүдийг урьдчилан сэргийлдэг
   - Жишээ: Хэрэглэгч-ийн процесс системийн файлыг санамсаргүйгээр устгаж чадахгүй

### Хүүхэлсүүлэлтийн сүлжээ (Defense in Depth)

```
Түвшин 1: Орон тоот нэвтрэх (User Authentication)
    └─ Нэвтрэлтийн нь нэр, нууц үг, SSH түлхүүр ...

[image1]: <data:image/png;base64,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>
Түвшин 2: Файлын системийн зөвшөөрөл (File System Permissions)
    └─ Үгүйсгэх эрх ба нэвтрэх эрхийн хамгаалалт

Түвшин 3: Үйлчилгээний удирдлага (Service Management)
    └─ Аль процесс ямар хэрэглэгчээр гүйцэтгэх (Process Execution)

Түвшин 4: Сүүлийн үйлдлийн бүртгэл (Audit Logging)
    └─ Аль процесс ямар файлуудыг хүүхэлсүүлэлсийн бүртгэл (Access Logging)
```

### Файлын системийн зөвшөөрлөөс үүсдэг аюулгүй байдлын хамгаалалт

| Аюулгүй байдлын элемент | Linux Permission-ийн нөлөө | Жишээ |
|-------------------------|---------------------------|--------|
| Өгөгдлийн нууцлал | Бичиж болохгүй файл эхлүүлнэ | `chmod 400` - root-г эс тэгвэл унршахгүй |
| Системийн бүрэн байдал | Критик системийн файлыг хамгаалдаг | `/etc/sudoers`, `/etc/shadow` |
| Үйлчилгээний аюулгүй байдал | Web/DB серверийн мэдээллийг нэг бүлэгт оруулах | `chgrp nginx /var/www/html` |
| Хэрэглэгчийн хамгаалалт | Хэрэглэгчдийн файлуудыг нэг нэгээр нь хамгаалдаг | `chmod 700 ~/.ssh` |

## 3.2 Эрсдэл, эмзэг байдал {#эрсдэл-эмзэг-байдал}

### Файлын системийн зөвшөөрлийн үндсэн эрсдэл

#### 1. Хэт өргөн зөвшөөрөл (Over-Permissive Permissions)

**Асуудал:**
```bash
chmod 777 sensitive_file.txt
# Хэн ч уршдаж, бичиж, гүйцэтгэх боломжтой
```

**Үр дагавал:**
- Аль ч хэрэглэгч файлыг санамсаргүйгээр эсвэл зохиомжтой өөрчилж болно
- Өгөгдлийн үнэлгээ эсвэл нүүцэлдлэг шилжүүлэх эрсдэл
- Вирус эсвэл нүүцэллэг програм файлын системийг эрч хүүхэлсүүлэлж болно

#### 2. Дефолт umask хэтрүүлэлт

**Асуудал:**
```bash
umask 0000  # Сохор
# Үүсэх файлууд: -rw-rw-rw- (666)
```

#### 3. Sudo хүүхэлсүүлэлтийн сэхэтгэл

**Асуудал:**
```bash
user ALL=(ALL) NOPASSWD: ALL
# Хэрэглэгч нууц үг оруулалгүй root болно
```

#### 4. Symlink攻击

**Асуудал:**
```bash
ln -s /etc/shadow /tmp/important_file
cat /tmp/important_file
# Нүүц үгийн hash-г уршдаж болно
```

---

# 4. Жишээ ба практик ашигласан байдал {#жишээ}

## Linux дээрх практик жишээнүүд

### Жишээ 1: Файлын зөвшөөрлийн шалгалт

```bash
ls -l myfile.txt
# -rw-r--r-- 1 user group 1234 Jan 15 10:30

stat myfile.txt
```

### Жишээ 2: SSH хувийн түлхүүрийн хамгаалалт

```bash
chmod 600 ~/.ssh/id_rsa       # -rw------- 
chmod 644 ~/.ssh/id_rsa.pub   # -rw-r--r--
chmod 700 ~/.ssh              # drwx------
chmod 600 ~/.ssh/config       # -rw-------
```

### Жишээ 3: Дефолт umask тогтоол

```bash
umask 0077      # Зөвхөн эзэмшигч
touch example.txt
ls -l example.txt  # -rw------- 
```

### Жишээ 4: Web серверийн файлын эрхүүдийн тохиргоо

```bash
chmod 755 /var/www/html
find /var/www/html -type f -exec chmod 644 {} \;
find /var/www/html -type d -exec chmod 755 {} \;
sudo chown -R www-data:www-data /var/www/html
```

### Жишээ 5: Эрсдэлтэй файлуудын хайлт

```bash
find / -type f -perm -002 2>/dev/null  # Нийтээр уршдах файл
find / -perm -4000 -type f 2>/dev/null  # SUID файлуудын хайлт
find / -perm -2000 -type f 2>/dev/null  # SGID файлуудын хайлт
```

---

# 5. Давуу ба сул тал {#давуу-ба-сул-тал}

## Давуу талууд

| Давуу тал | Тайлбар | Жишээ |
|----------|--------|--------|
| **Энгийн ба ойлгомжтой** | r, w, x гурван эрхүүд | `chmod 755` |
| **Сервер нь хүргүүлдэг** | Алсаас удирдах боломж | SSH удалаадаа өөрчлөх |
| **Хэрэглэгчийн хамгаалалт** | Бие даан хэрэглэгчдийг хамгаалдаг | /home/user1 нээлттэй биш |
| **Системийн бүрэн байдал** | Критик файлуудыг хамгаалдаг | /etc/shadow хамгаалалттай |
| **Стандарт үйлдлийн системүүдэд** | UNIX, Linux, macOS, BSD | Нийтлэг мэдлэг |

## Сул талууд

| Сул тал | Асуудал | Шийдвэр |
|--------|--------|---------|
| **Бүлгийн хүүхэлсүүлэлтийн сүүдлэнэ** | Нэг бүлгийн бүх хэрэглэгч нэг эрхтэй | ACL ашиглах |
| **ACL хүргүүлэхгүй** | Нэг хэрэглэгчийн хэдэн төрлийн эрх | `setfacl` ашиглах |
| **SUID/SGID сопастальна** | Root болж ямар ч файл унршдаж болно | Эхлүүлэлтийн үхлийг нүүдлэнэ |
| **Symlink攻击 эрсдэл** | /tmp-д үлүү анхаарал өгөхгүй | mktemp() ашиглах |
| **Бүлгийн тоо хязгаарлагдсан** | Ирэх хэрэглэгч 32 бүлэг хамгаалж болно | LDAP эсвэл RADIUS ашиглах |

---

# 6. Хамгаалах арга / зөв хэрэглээ {#хамгаалах-арга-зөв-хэрэглээ}

## Админы анхаарах зүйл

### 1. Дефолт umask-ийг сүүдлэнэ

```bash
# .bashrc эсвэл .profile файлд
umask 0077     # Зөвхөн эзэмшигч

# Системийн /etc/profile-д
umask 0027     # Бүлэг уршдах, бусад-ийн эрх биш
```

### 2. Эрхүүдийн сүүдлэнэ ба мониторинг

```bash
sudo auditctl -w /etc/ -p wa -k etc_changes
sudo find / -perm -002 -type f 2>/dev/null
sudo find / -perm -4000 -type f 2>/dev/null
```

### 3. Sticky bit ба Special permissions

```bash
sudo chmod 1777 /tmp     # drwxrwxrwt
sudo chmod u+s /usr/bin/sudo   # SUID
sudo chmod g+s /var/www/html   # SGID
```

### 4. Критик файлуудын хамгаалалт

```bash
chmod 644 /etc/passwd
chmod 000 /etc/shadow
chmod 440 /etc/sudoers
chmod 600 ~/.ssh/authorized_keys
```

## Хэрэглэгчийн Best Practice

```bash
# SSH түлхүүр хамгаалалт
chmod 700 ~/.ssh
chmod 600 ~/.ssh/id_rsa
chmod 644 ~/.ssh/id_rsa.pub

# Үй бүлэг файлуудын хамгаалалт
chmod 600 ~/.bashrc
chmod 600 ~/.bash_history
chmod 600 ~/.ssh/config

# Үй сумбуу програм
chmod 700 ~/.local/bin
chmod 755 ~/.local/bin/my_script
```

### Аюулгүй Temp файлын үүсгэлт

```bash
TMPFILE=$(mktemp)      # Үүсгэл
# ... ажил хийх ...
rm "$TMPFILE"          # Устгах
```

---

# 7. Дүгнэлт {#дүгнэлт}

## Үндсэн дүгнэлт

Linux файлын системийн зөвшөөрлийн загвар нь:

1. **Аюулгүйдийн үндсэн үзүүлэлт**
   - UNIX философиясаас авсан энгийн гэвч үр дүнтэй хамгаалалтын үйлдэл
   - Файлын зөвшөөрлийн эрхүүдийг эзэмшигч, бүлэг, бусдаар ялгаруулж удирдаж байна
   - Эзэмшигчийн UID, GID болон permission bits нь файлын хүүхэлсүүлэлтийг тодорхойлдог

2. **Практик ач холбогдол**
   - Web серверүүд, датабазын серверүүд, файлын серверүүдэд чухал үүрэг гүйцэтгэнэ
   - Түрүүлэгч хэрэглэгчдийн өгөгдлийг хамгаалдаг
   - Системийн бүрэн байдлыг хамгаалдаг

3. **Эрсдэл ба хамгаалалт**
   - Хэт өргөн зөвшөөрөл (777) нь эрсдэлтэй
   - Дефолт umask-ийн сүүдлэнэ чухал
   - SUID, SGID, sticky bit-ийн эхлүүлэлт хэрэгтэй

4. **Best Practice**
   - Дефолт umask = 0077
   - SSH түлхүүр хамгаалалт: `chmod 600 ~/.ssh/id_rsa`
   - /tmp файлуудыг mktemp()-аар үүсгэнэ
   - Мониторинг ба аудит хэрэгтэй

5. **OS Security хамгаалалт**
   - Файлын системийн зөвшөөрөл нь многоуровневой хамгаалалтын үндсэн элемент
   - CIA үндсэн зарчим (Confidentiality, Integrity, Availability) хамгаалдаг
   - Linux-ийн аюулгүйдлийн үндсэн цоолсон үндэс юм

---

# 8. Ашигласан эх сурвалж {#ашигласан-эх-сурвалж}

## Үндсэн эх сурвалж

1. **The Linux Foundation - Linux File Permissions**
   - https://www.linux.com/
   - File permissions, ownership, Unix philosophy

2. **GNU Coreutils Manual**
   - https://www.gnu.org/software/coreutils/manual/
   - chmod, chown, chgrp, stat команд

3. **Ubuntu Man Pages Online**
   - man chmod, man chown, man stat, man find
   - Бүх Linux командын бүрэн баримт

4. **Linux Permissions in a Nutshell**
   - Author: Chris Hoover
   - rwx permissions, umask, SUID, SGID, Sticky Bit

5. **Linux Security Cookbook (O'Reilly)**
   - Chapter: File Permissions and Access Control
   - Real-world security scenarios

6. **NIST Cybersecurity Framework**
   - File integrity and access controls
   - Compliance requirements

7. **The Linux Programming Interface - Michael Kerrisk**
   - File I/O and Permissions
   - Comprehensive guide to Linux system APIs

## Нэмэлт материал

- RedHat Enterprise Linux Documentation
- CentOS Security Guides  
- Debian File Systems Security
- ACL (Access Control List) ба SELinux documentation

---

## Нэмэлт уломжлал

Энэ бие даалтын тайлан Linux файлын системийн зөвшөөрлийн загварын үндсэн ойлголт, практик ашигласан байдал, аюулгүй байдлын эрсдэл ба хамгаалах аргууд агуулагдаж байна. 

**Үндсэн сэдвүүд:**
- Файлын системийн зөвшөөрлийн архитектур
- Unix-ийн UID/GID хүүхэлсүүлэлтийн систем
- CIA үндсэн зарчим (Нууцлал, Бүрэн байдал, Бэлэн байдал)
- Дефолт umask ба зөвшөөрлийн тохиргоо
- SSH, Web, Database серверийн аюулгүйдийн практик
- Аудит ба мониторинг техник

**Ашигласан техникүүд:**
- Command-line tools (chmod, chown, stat, find)
- Permission checking ба auditing
- Best practices ба security hardening

---

*Тайланг 2026 оны 4 сарын 15-нд гүйцэтгэлээ*
