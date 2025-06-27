# Student Management（受講生管理システム）

## 概要

受講生の個人情報や受講コース、申込状況を管理するWebアプリケーションです。<br>
受講生・受講コース・申込状況を紐付けて扱い、検索・登録・更新といった処理を行います。

## 作成背景

様々なWebアプリケーションで使用されるCRUD処理を実装し、バックエンドの知識や設計力を深めたいと考え作成しました。<br>
実装からテスト・デプロイまで一貫して取り組んだ成果物を提示できるよう構成しています。

## 工夫点

- Controller、Service、Repository層で役割を明確に分担し、今後の機能追加や保守がしやすい構成
- リストの結合（StudentConverter）によるドメイン（StudentDetail）の組み立て

## 使用技術・ライブラリ

| 分類     | 技術スタック                                                      |
|--------|-------------------------------------------------------------|
| バックエンド | Java 21 / Spring Boot 3.4.5                                 |
| ORマッパー | MyBatis                                                     |
| ビルド    | Gradle                                                      |
| テスト    | JUnit5 / Mockito / AssertJ / MyBatis Test / Bean Validation |
| CI/CD  | GitHub Actions / Amazon EC2 / systemd                       |
| データベース | H2（テスト用） / MySQL（本番想定）                                      |

## 機能一覧

- 受講生検索（全件・ID指定・条件指定）
- 受講生登録（コース情報・申込状況を含む）
- 受講生更新（コース情報・申込状況を含む）
- 受講生削除（論理削除）
- 入力バリデーション（形式・必須・制約チェック）

## API

| HTTPメソッド | URL                                 | 処理内容                                  | 
|---------------|-------------------------------------|---------------------------------------|
| GET           | /students                           | 受講生詳細の全件取得 | 
| GET           | /students/{id}                      | 指定したIDの受講生詳細の取得                       |
| POST          | /students/search | 検索条件に合致する受講生詳細の取得       | 
| POST          | /students                           | 受講生詳細の新規登録                           |
| PUT           | /students                           | 受講生詳細の更新  （論理削除を含む）                            |

## 動作イメージ

### 受講生検索（全件）

https://github.com/user-attachments/assets/16181e5f-5424-48e2-8286-6f69fccb7a09

### 受講生検索（ID）

https://github.com/user-attachments/assets/ef06325b-8f6c-4573-b3ff-5c537f3a4c38

### 受講生検索（条件指定）

https://github.com/user-attachments/assets/04700555-72cf-4a43-81a2-2dcf00d40fb0

### 受講生登録

https://github.com/user-attachments/assets/de0e2240-5f8b-460c-a89c-99e7e7018035

### 受講生更新（論理削除を含む）

https://github.com/user-attachments/assets/de438c48-6099-43dd-b7c4-ca113013b203

## テスト実装

| レイヤー        | 主なテスト内容                  | 使用技術                             |
|-------------|--------------------------|----------------------------------|
| Controller層 | HTTPリクエスト、バリデーション、JSON検証 | MockMvc / WebMvcTest / Validator |
| Service層    | 業務ロジック、例外発生、引数の整合性       | JUnit5 / Mockito                 |
| Repository層 | SQLマッピングとDB操作の検証         | MyBatis Test / H2 DB             |

- 異常系含む網羅的なバリデーションテスト
- Mockitoによる依存オブジェクトのスタブ化で純粋なユニットテストを実現

## CI / CD パイプライン構成

GitHub Actions により、テスト → ビルド → EC2への自動デプロイを実現しています。

### 構成概要

| 項目    | 内容                                     |
|-------|----------------------------------------|
| トリガー  | `main`ブランチへの push / PR                 |
| ビルド   | `./gradlew bootJar`                    |
| テスト   | `./gradlew test`                       |
| デプロイ先 | Amazon EC2 (Amazon Linux 2023)         |
| 起動管理  | systemd による `StudentManagement` サービス制御 |

## 今後の展望

* 認証機能の追加
* HTTPSプロトコルへの対応
* フロントエンド実装（Thymeleaf, Vue.js, React など）
