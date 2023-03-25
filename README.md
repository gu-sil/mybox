# mybox
Spring Webflux를 통해 파일 업로드, 다운로드를 구현한 프로젝트입니다.<br>
[넘블 챌린지](https://www.numble.it/d117019e-a9ca-40bc-ac30-907859d74fa5)에서 시작했습니다.

## 시스템 아키텍쳐
![image](https://user-images.githubusercontent.com/58730856/227726143-248d6787-554d-4839-8786-dcba058218dc.png)
- NCP에서 배포합니다.
- DB, File Storage 전부 한 인스턴스가 담당합니다.

## CI/CD
![image](https://user-images.githubusercontent.com/58730856/227726297-d880598e-aa45-4d3f-995e-f4e351f93098.png)
- CI/CD는 Github Action을 사용합니다.

## API
[API 문서](https://github.com/gu-sil/mybox/blob/main/docs/api.md)

## ERD
![image](https://user-images.githubusercontent.com/58730856/227727256-bcb1a4d6-bba5-4487-9d32-3902c6ce67b4.png)
