### 🕒 2026-08-07 23:16:21 KST
Merge pull request #31 from Lesser-zzz/sprite

Sprite

---

### 🕒 2026-08-07 22:07:01 KST
Update DEVLOG.md

---

### 🕒 2026-08-07 00:18:17 KST
Update DEVLOG.md

---

### 🕒 2026-08-06 23:20:37 KST
Update Plan.md

---

### 🕒 2026-08-06 22:41:23 KST
일단 구상만 추가해봅시다잉

---

### 🕒 2026-08-06 00:12:58 KST
실수로 master에 sprite 내용 오려버려서 롤백함

ㅇㅇ

---

### 🕒 2026-08-06 00:05:56 KST
몰라 오류래서 수정함

---

### 🕒 2026-08-05 23:38:58 KST
Update DEVLOG.md

---

### 🕒 2026-08-05 23:03:42 KST
Merge pull request #30 from Lesser-zzz/develop-cheat

맵핵지도 개쎈반지 개빠른반지 개센검 추가

---

### 🕒 2026-08-05 22:14:47 KST
Update DEVLOG.md

---

### 🕒 2026-08-05 20:53:20 KST
사인 넣으려고 지랄중

- name: Sign Android Release APK (Native)
      if: github.event_name == 'workflow_dispatch' && (inputs.build_target == 'android' || inputs.build_target == 'all') && inputs.build_type == 'release'
      shell: bash
      run: |
        echo "${{ secrets.KEYSTORE_BASE64 }}" | base64 --decode > keystore.jks
        
        # 툴을 직접 찾아서 경로 지정 (버전 오탐지 원천 차단)
        APKSIGNER=$(find "$ANDROID_HOME/build-tools" -name "apksigner.bat" -o -name "apksigner" | head -n 1)
        ZIPALIGN=$(find "$ANDROID_HOME/build-tools" -name "zipalign.exe" -o -name "zipalign" | head -n 1)
        
        echo "Found apksigner: $APKSIGNER"
        echo "Found zipalign: $ZIPALIGN"
        
        APK_PATH=$(find android/build/outputs/apk/release -name "*.apk" | head -n 1)
        echo "Found APK: $APK_PATH"
        
        "$ZIPALIGN" -v -p 4 "$APK_PATH" aligned.apk
        "$APKSIGNER" sign --ks keystore.jks \
          --ks-key-alias "${{ secrets.KEY_ALIAS }}" \
          --ks-pass "pass:${{ secrets.STORE_PASSWORD }}" \
          --key-pass "pass:${{ secrets.KEY_PASSWORD }}" \
          aligned.apk
          
        mv aligned.apk "$APK_PATH"
        echo "Successfully signed Android Release APK!"

---

### 🕒 2026-08-05 20:46:33 KST
SDK에 미리 깔아야하는데 빼묵었대

---

### 🕒 2026-08-05 20:39:58 KST
야매 제미나이방식 말고 챗지피티 방식으로./.

---

### 🕒 2026-08-05 20:27:13 KST (Branch: `main (Direct Push)`)
- **잘못 붙여넣음 ㅎㅎ** (80bbddc83)
  

---

### 🕒 2026-08-05 20:16:58 KST (Branch: `main (Direct Push)`)
- **구식버전 안 쓰도록 34.0.0 명시** (a6e084dc9)
  

---

### 🕒 2026-08-05 20:07:29 KST (Branch: `main (Direct Push)`)
- **사인 만들었는데 반영을 안 해서 로직 수정함** (1d39a3b59)
  

---

### 🕒 2026-08-05 19:45:37 KST (Branch: `main (Direct Push)`)
- **Update appVersionName to '0.0.3'** (c94165b25)
  

---

### 🕒 2026-08-05 19:37:35 KST (Branch: `main (Direct Push)`)
- **시간 제대로 못 맞추는거 고침** (78577ca2a)
  

---

### 🕒 2026-08-05 10:35:03 KST (Branch: `main (Direct Push)`)
- **앱패키지 서명 릴리즈 apk 빌드 시 찍도록 설정** (955da7ce4)
  이거면 패키징 오류 사라질...덧?

---

### 🕒 2026-08-05 10:17:57 KST (Branch: `main (Direct Push)`)
- **Update build_CICD.yml** (0e6b12e59)
  

---

### 🕒 2026-08-05 10:13:17 KST
Merge pull request #29 from Lesser-zzz/sprite

스프라이트 바꾼 거 옮김

---

### 🕒 2026-08-05 07:51:45 KST
Merge pull request #28 from Lesser-zzz/develop

L키 레벨업 치트 / 도그파이트 특성연결 안되던거 수정 / 

---

### 🕒 2026-08-05 07:16:23 KST
debug버전에서만 치트 적용되도록 바꿔봄

---

### 🕒 2026-08-04 15:03:01 KST
Update DEVLOG.md

---

### 🕒 2026-08-04 13:53:07 KST
Update build_CICD.yml

---

# 변경 내역 (Changelog)

모든 주요 변경 사항은 이 파일에 기록됩니다.
이 프로젝트는 [Keep a Changelog](https://keepachangelog.com/ko/1.0.0/)의 형식을 따릅니다.

## [v0.0.1] - 2026-07-27 (프로토타입 배포)

### 추가됨 (Added)
- **첫 번째 실험체 '현우' 뼈대 추가** (전사 클래스 대체)

- 패시브 스킬 '도그파이트' 기본 로직 구현
  - 공격 명중 시 스택 +1 증가 (최대 10스택)
  - 10스택 시 다음 공격에 추가 피해(+5) 및 체력 회복 발동

### 변경됨 (Changed)


### 수정됨 (Fixed)
- (아직 보고된 버그 없음)
